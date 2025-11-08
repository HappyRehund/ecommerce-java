package com.rehund.ecommerce.controller;

import com.rehund.ecommerce.common.errors.ForbiddenAccessException;
import com.rehund.ecommerce.model.UserInfo;
import com.rehund.ecommerce.model.UserResponse;
import com.rehund.ecommerce.model.UserUpdateRequest;
import com.rehund.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        UserResponse userResponse = UserResponse.fromUserAndRoles(userInfo.getUser(), userInfo.getRoles());
        return ResponseEntity.ok(userResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest
    ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        if(!Objects.equals(userInfo.getUser().getUserId(), id) && !userInfo.getAuthorities().contains("ROLE_ADMIN")){
            throw new ForbiddenAccessException(
                    "user " + userInfo.getUsername() + " not allowed to update"
            );
        }

        UserResponse updatedUser = userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser( @PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        // user akan masuk ke kondisi jika dia bukan admin dan bukan pemilik id, klo dia admin atau pemilik id maka ga masuk ke lubang Exception
        if(!Objects.equals(userInfo.getUser().getUserId(), id) && !userInfo.getAuthorities().contains("ROLE_ADMIN")){
            throw new ForbiddenAccessException(
                    "user " + userInfo.getUsername() + " not allowed to delete"
            );
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
