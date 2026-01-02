/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.authservice.service;

import com.example.authservice.dto.*;
import com.example.authservice.exception.AuthException;
import com.example.authservice.exception.ResourceNotFoundException;
import com.example.authservice.model.Permission;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.PermissionRepository;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for: {}", request.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail()
        ).orElseThrow(() -> new AuthException("Invalid credentials"));

        // Check if account is locked
        if (user.getIsLocked()) {
            log.warn("Login attempt for locked account: {}", user.getUsername());
            throw new AuthException("Account is locked. Please contact support.");
        }

        // Check if account is active
        if (!user.getIsActive()) {
            log.warn("Login attempt for inactive account: {}", user.getUsername());
            throw new AuthException("Account is not active");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.incrementFailedLoginAttempts();
            userRepository.save(user);
            log.warn("Failed login attempt for: {}", user.getUsername());
            throw new AuthException("Invalid credentials");
        }

        // Reset failed login attempts on successful login
        user.resetFailedLoginAttempts();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        log.info("Successful login for user: {}", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .user(mapToUserInfo(user))
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registration attempt for username: {}, email: {}",
                request.getUsername(), request.getEmail());

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email is already registered");
        }

        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .userType(request.getUserType())
                .isActive(true)
                .isVerified(false)
                .isLocked(false)
                .failedLoginAttempts(0)
                .twoFactorEnabled(false)
                .roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();

        // Assign roles
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            roles = roleRepository.findByNameIn(request.getRoles());
            if (roles.isEmpty()) {
                throw new AuthException("Invalid roles provided");
            }
        } else {
            // Assign default role based on user type
            String defaultRole = getDefaultRoleForUserType(request.getUserType());
            Role role = roleRepository.findByName(defaultRole)
                    .orElseThrow(() -> new AuthException("Default role not found: " + defaultRole));
            roles.add(role);
        }
        user.setRoles(roles);

        // Generate verification token
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Generate tokens
        String token = jwtUtil.generateToken(savedUser);
        String refreshToken = jwtUtil.generateRefreshToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .user(mapToUserInfo(savedUser))
                .build();
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .message("Invalid token")
                        .build();
            }

            String username = jwtUtil.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AuthException("User not found"));

            if (!user.getIsActive() || user.getIsLocked()) {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .message("User account is not active")
                        .build();
            }

            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            Set<String> permissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(Permission::getName)
                    .collect(Collectors.toSet());

            return TokenValidationResponse.builder()
                    .valid(true)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .userId(user.getId())
                    .userType(user.getUserType())
                    .roles(roles)
                    .permissions(permissions)
                    .message("Token is valid")
                    .build();

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token validation failed: " + e.getMessage())
                    .build();
        }
    }

    @Transactional
    public ApiResponse requestPasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // TODO: Send email with reset token
        log.info("Password reset requested for user: {}", user.getUsername());

        return ApiResponse.success("Password reset link has been sent to your email");
    }

    @Transactional
    public ApiResponse resetPassword(PasswordResetConfirmRequest request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new AuthException("Invalid or expired reset token"));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password reset successful for user: {}", user.getUsername());

        return ApiResponse.success("Password has been reset successfully");
    }

    @Transactional
    public ApiResponse changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password changed for user: {}", user.getUsername());

        return ApiResponse.success("Password changed successfully");
    }

    @Transactional
    public ApiResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AuthException("Invalid verification token"));

        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthException("Verification token has expired");
        }

        user.setIsVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", user.getUsername());

        return ApiResponse.success("Email verified successfully");
    }

    private AuthResponse.UserInfo mapToUserInfo(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return AuthResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userType(user.getUserType())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    private String getDefaultRoleForUserType(com.example.authservice.model.UserType userType) {
        return switch (userType) {
            case PATIENT -> "ROLE_PATIENT";
            case DOCTOR -> "ROLE_DOCTOR";
            case ADMIN -> "ROLE_ADMIN";
            case STAFF -> "ROLE_STAFF";
            case PHARMACIST -> "ROLE_PHARMACIST";
            case RECEPTIONIST -> "ROLE_RECEPTIONIST";
        };
    }
}
