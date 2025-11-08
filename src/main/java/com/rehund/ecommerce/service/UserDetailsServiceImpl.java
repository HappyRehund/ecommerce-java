package com.rehund.ecommerce.service;

import com.rehund.ecommerce.common.errors.UserNotFoundException;
import com.rehund.ecommerce.entity.Role;
import com.rehund.ecommerce.entity.User;
import com.rehund.ecommerce.model.UserInfo;
import com.rehund.ecommerce.repository.RoleRepository;
import com.rehund.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByKeyword(username)
                .orElseThrow(() -> new UserNotFoundException("user with username" + username + "not found"));

        List<Role> roles = roleRepository.findByUserId(user.getUserId());

        return UserInfo.builder()
                .user(user)
                .roles(roles)
                .build();
    }

    // Method ini return UserInfo yang merupakan implementasi UserDetails, dimana berisi informasi user

}
