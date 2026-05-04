package com.bookstore.user.controller;

import com.bookstore.user.dto.*;
import com.bookstore.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @Operation(summary = "Login and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @Operation(summary = "Get authenticated user profile")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getProfile(auth.getName()));
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication auth,
            @Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(
                userService.updateProfile(auth.getName(), request));
    }

    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication auth,
            @RequestParam String newPassword) {
        userService.changePassword(auth.getName(), newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @Operation(summary = "Delete user account")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}