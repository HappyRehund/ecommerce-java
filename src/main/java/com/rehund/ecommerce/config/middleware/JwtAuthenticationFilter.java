package com.rehund.ecommerce.config.middleware;

import com.rehund.ecommerce.service.JwtService;
import com.rehund.ecommerce.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// Intinya ini middleware yang akan berjalan sebelum masuk ke controller
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // dapetin Header
        final String authHeader = request.getHeader("Authorization");

        // check kondisi Header klo kosong pergi ke next filter chain
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            try {
                filterChain.doFilter(request, response);
            } catch (InsufficientAuthenticationException exception){
                handlerExceptionResolver.resolveException(request, response, null, exception);
            }
            return;
        }

        // klo guard clause lolos

        try{
            // Ambil / Ekstraksi jwt token
            // Bearer <jwt token>
            // Bearer = 6 kata (0 - 5), whitespace (6), huruf pertama token (7)
            final String jwt = authHeader.substring(7);

            // validasi token klo gagal masuk filterChain berikutnya
            if(jwtService.validateToken(jwt)){

                // mendapatkan username dari token
                final String userIdentifier = jwtService.getUsernameFromToken(jwt);

                // "Pemegang" statis global Spring Security. Di sinilah disimpan informasi otentikasi pengguna yang saat ini "login" untuk request ini.
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // check token JWT ini valid dan berisi nama pengguna AND pengguna ini belum diautentikasi untuk request yang sedang berlangsung
                if(userIdentifier != null && authentication == null){

                    //panggil method loadUserByUsername untuk mendapatkan userDetails (User dan Roles)
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);

                    // objek otentikasi sesuai aturan Spring Security yang berisi UserDetails dan Authorities (Roles)
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );


                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Me-login-kan user ke security context untuk satu request
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

                filterChain.doFilter(request, response);
            }

        } catch (Exception exception){
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

    }
}
