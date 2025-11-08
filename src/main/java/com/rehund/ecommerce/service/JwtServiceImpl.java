package com.rehund.ecommerce.service;

import com.rehund.ecommerce.common.DateUtil;
import com.rehund.ecommerce.config.JwtSecretConfig;
import com.rehund.ecommerce.model.UserInfo;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService{

    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signKey;

    @Override
    public String generateToken(UserInfo userInfo) {

        LocalDateTime expirationTime = LocalDateTime.now().plus(jwtSecretConfig.getJwtExpirationTime());
        Date expirationDate = DateUtil.convertLocalDateTimeToDate(expirationTime);

        // Jwts tuh bisa untuk buat token atau memeriksa token
        // Yang ini buat token (builder)
        return Jwts.builder()
                .subject(userInfo.getUsername())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(signKey)
                .compact();

    }

    @Override
    public boolean validateToken(String token) {

        // penjelasan dikit
        // JWT itu String (token), dimana terdiri dari 3 bagian
        // Header, Payload (Claims), dan Signature

        try {
            // Ini tuh proses baca token (parser)
            // Jws = Signed JWT
            Jws<Claims> parsed = Jwts.parser()
                    .verifyWith(signKey)
                    .build()
                    .parseSignedClaims(token);

            Header header = parsed.getHeader();
            Claims claims = parsed.getPayload();

            return true;
        } catch (JwtException | IllegalArgumentException e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token);

        return parsed.getPayload().getSubject();
    }
}
