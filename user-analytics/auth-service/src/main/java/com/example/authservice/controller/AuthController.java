/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.authservice.controller;

import com.example.authservice.dto.*;
import com.example.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login request received for: {}", request.getUsernameOrEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request received for: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @Valid @RequestBody TokenValidationRequest request) {
        log.info("Token validation request received");
        TokenValidationResponse response = authService.validateToken(request.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-header")
    public ResponseEntity<TokenValidationResponse> validateTokenFromHeader(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Token validation request received from header");
        String token = authHeader.replace("Bearer ", "");
        TokenValidationResponse response = authService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());
        ApiResponse response = authService.requestPasswordReset(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<ApiResponse> confirmPasswordReset(
            @Valid @RequestBody PasswordResetConfirmRequest request) {
        log.info("Password reset confirmation request");
        ApiResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Password change request for user: {}", userId);
        ApiResponse response = authService.changePassword(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        log.info("Email verification request");
        ApiResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running");
    }
}
