package com.rehund.ecommerce.service;

import com.rehund.ecommerce.common.errors.*;
import com.rehund.ecommerce.entity.Role;
import com.rehund.ecommerce.entity.User;
import com.rehund.ecommerce.entity.UserRole;
import com.rehund.ecommerce.model.UserRegisterRequest;
import com.rehund.ecommerce.model.UserResponse;
import com.rehund.ecommerce.model.UserUpdateRequest;
import com.rehund.ecommerce.repository.RoleRepository;
import com.rehund.ecommerce.repository.UserRepository;
import com.rehund.ecommerce.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    //Repository
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    // Passwd
    // tidak perlu membuat new BcryptPasswordEncoder (udah terpusat disini)
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest registerRequest) {

        if(existsByUsername(registerRequest.getUsername())){
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if(existsByEmail(registerRequest.getEmail())){
            throw new EmailAlreadyExistsException("Email is already used");
        }

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirmation())){
            throw new BadRequestException("Password confirmation doesn't match");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .enabled(true)
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role doesn't exist"));

        UserRole userRoleRelation = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getUserId(), role.getRoleId()))
                .build();
        userRoleRepository.save(userRoleRelation);

        return UserResponse.fromUserAndRoles(user, List.of(role));
    }

    @Override
    public UserResponse findById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found with id " + id));

        List<Role> roles = roleRepository.findByUserId(existingUser.getUserId());

        return UserResponse.fromUserAndRoles(existingUser, roles);
    }

    @Override
    public UserResponse findByKeyword(String keyword) {
        User existingUser = userRepository.findByKeyword(keyword)
                .orElseThrow(() -> new UserNotFoundException("user not found with username/email " + keyword));

        List<Role> roles = roleRepository.findByUserId(existingUser.getUserId());

        return UserResponse.fromUserAndRoles(existingUser, roles);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found with id " + id));

        if(request.getCurrentPassword() != null && request.getNewPassword() != null){
            if(!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())){
                throw new InvalidPasswordException("Wrong password");
            }

            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            existingUser.setPassword(encodedPassword);
        }

        if (request.getUsername() != null && !request.getUsername().equals(existingUser.getUsername())){
            if(existsByUsername(request.getUsername())){
                throw new UsernameAlreadyExistsException("Username " + request.getUsername() + " is already taken");
            }

            existingUser.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())){
            if(existsByEmail(request.getEmail())){
                throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already used");
            }

            existingUser.setUsername(request.getUsername());
        }

        userRepository.save(existingUser);

        List<Role> roles = roleRepository.findByUserId(existingUser.getUserId());
        return UserResponse.fromUserAndRoles(existingUser, roles);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found with id " + id));

        userRoleRepository.deleteByIdUserId(existingUser.getUserId());

        userRepository.delete(existingUser);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
