package com.rehund.ecommerce.service;

import com.rehund.ecommerce.model.UserInfo;

public interface JwtService {
    String generateToken(UserInfo userInfo);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
