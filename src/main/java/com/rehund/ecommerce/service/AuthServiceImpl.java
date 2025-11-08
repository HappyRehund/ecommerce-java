package com.rehund.ecommerce.service;

import com.rehund.ecommerce.common.errors.InvalidPasswordException;
import com.rehund.ecommerce.model.AuthRequest;
import com.rehund.ecommerce.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;

    @Override
    public UserInfo authenticate(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            return (UserInfo) authentication.getPrincipal(); // nnti isinya UserDetails
        } catch (Exception ex){
            log.error(ex.getMessage());
            throw new InvalidPasswordException("Invalid username or password");
        }
    }
}
