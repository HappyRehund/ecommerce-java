package com.rehund.ecommerce.service;

import com.rehund.ecommerce.model.AuthRequest;
import com.rehund.ecommerce.model.UserInfo;

public interface AuthService {
    UserInfo authenticate(AuthRequest authRequest);
}
