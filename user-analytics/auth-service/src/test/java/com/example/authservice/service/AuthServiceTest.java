/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.authservice.service;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.exception.AuthException;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.model.UserType;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Auth Service Tests")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
                .id(1L)
                .name("ROLE_PATIENT")
                .description("Patient role")
                .permissions(new HashSet<>())
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .userType(UserType.PATIENT)
                .isActive(true)
                .isVerified(false)
                .isLocked(false)
                .failedLoginAttempts(0)
                .roles(Set.of(testRole))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testSuccessfulLogin() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(jwtUtil.getExpirationTime()).thenReturn(86400000L);

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUser().getUsername()).isEqualTo("testuser");

        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any(User.class));
        verify(jwtUtil).generateRefreshToken(any(User.class));
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .usernameOrEmail("testuser")
                .password("wrongpassword")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void testLoginWithLockedAccount() {
        // Arrange
        testUser.setIsLocked(true);
        AuthRequest request = AuthRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("Account is locked. Please contact support.");
    }

    @Test
    void testLoginWithInactiveAccount() {
        // Arrange
        testUser.setIsActive(false);
        AuthRequest request = AuthRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("Account is not active");
    }

    @Test
    void testSuccessfulRegistration() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("password123")
                .firstName("New")
                .lastName("User")
                .userType(UserType.PATIENT)
                .build();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(jwtUtil.getExpirationTime()).thenReturn(86400000L);

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("access-token");
        assertThat(response.getUser()).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegistrationWithExistingUsername() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("newuser@example.com")
                .password("password123")
                .userType(UserType.PATIENT)
                .build();

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("Username is already taken");
    }

    @Test
    void testRegistrationWithExistingEmail() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .email("test@example.com")
                .password("password123")
                .userType(UserType.PATIENT)
                .build();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("Email is already registered");
    }

    @Test
    void testFailedLoginIncrementsAttempts() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .usernameOrEmail("testuser")
                .password("wrongpassword")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(AuthException.class);

        verify(userRepository).save(argThat(user ->
                user.getFailedLoginAttempts() == 1
        ));
    }
}