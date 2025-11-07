package com.rehund.ecommerce.service;

import com.rehund.ecommerce.model.UserRegisterRequest;
import com.rehund.ecommerce.model.UserResponse;
import com.rehund.ecommerce.model.UserUpdateRequest;

public interface UserService {
    UserResponse register (UserRegisterRequest registerRequest);

    UserResponse findById(Long id);

    UserResponse findByKeyword(String keyword);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
