package com.example.demo.service;

import com.example.demo.dto.UserDetailDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRoles;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRolesRepository;
import com.example.demo.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRolesRepository userRolesRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final SecureRandom secureRandom = new SecureRandom();

    public UserResponseDTO loginUser(UserRequestDTO userDTO) {
        log.info("=== LOGIN ATTEMPT START ===");
        log.info("Email: {}", userDTO.getEmail());

        try {
            Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
            if (userOptional.isEmpty()) {
                log.warn("Login attempt for non-existent email");
                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            User user = userOptional.get();
            log.info("User found in DB:");
            log.info("  - Username: {}", user.getUsername());
            log.info("  - Email: {}", user.getEmail());
            log.info("  - Stored hash: {}", user.getPassword());
            log.info("  - Hash starts with: {}", user.getPassword().substring(0, 7));

            String plainPassword = userDTO.getPassword();
            String storedHash = user.getPassword();

            log.info("Password check:");
            log.info("  - Plain password length: {}", plainPassword.length());
            log.info("  - Plain password: {}", plainPassword); // Remove after debugging
            log.info("  - Stored hash length: {}", storedHash.length());

            // Test encoding the plain password
            String testHash = passwordEncoder.encode(plainPassword);
            log.info("  - Test encoding of plain password: {}", testHash);

            // Now do the actual match
            boolean matches = passwordEncoder.matches(plainPassword, storedHash);
            log.info("  - Match result: {}", matches);

            if (!matches) {
                log.warn("Password mismatch for user: {}", user.getUsername());

                // Additional debugging - check if stored password is actually encoded
                if (!storedHash.startsWith("$2a$") && !storedHash.startsWith("$2b$") && !storedHash.startsWith("$2y$")) {
                    log.error("CRITICAL: Stored password doesn't look like a BCrypt hash!");
                    log.error("  - Stored value: {}", storedHash);
                }

                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            log.info("Password matched successfully!");

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            if (userDetails == null) {
                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            String token = jwtUtil.generateToken(userDetails.getUsername());
            updateUserSignInDate(user);
            log.info("Login successful for user: {}", user.getUsername());
            log.info("=== LOGIN ATTEMPT END ===");

            return createUserResponseDTO(String.valueOf(user.getId()), token, "Login successful", "true");
        } catch (Exception e) {
            log.error("Failed to login: {}", e.getMessage(), e);
            return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
        }
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        log.info("=== SIGNUP ATTEMPT START ===");
        log.info("Email: {}", userDTO.getEmail());
        log.info("Username: {}", userDTO.getUsername());

        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return createUserResponseDTO("", "", "Signup failed: Email already exists", "false");
            }

            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                return createUserResponseDTO("", "", "Signup failed: Username already exists", "false");
            }

            Collection<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            if (roles.isEmpty()) {
                return createUserResponseDTO("", "", "Signup failed: Invalid roles", "false");
            }

            // Password encoding
            String plainPassword = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(plainPassword);
            String salt = generateSalt();

            log.info("Password encoding:");
            log.info("  - Plain password: {}", plainPassword); // Remove after debugging
            log.info("  - Plain password length: {}", plainPassword.length());
            log.info("  - Encoded password: {}", encodedPassword);
            log.info("  - Encoded length: {}", encodedPassword.length());
            log.info("  - Starts with: {}", encodedPassword.substring(0, 7));
            log.info("  - Generated salt: {}", salt);

            // Test that encoding/matching works
            boolean testMatch = passwordEncoder.matches(plainPassword, encodedPassword);
            log.info("  - Test match (should be true): {}", testMatch);

            if (!testMatch) {
                log.error("CRITICAL: Password encoder is not working correctly!");
                return createUserResponseDTO("", "", "Signup failed: Password encoding error", "false");
            }

            User user = new User.Builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .password(encodedPassword) // Make sure this is the ENCODED password
                    .salt(salt)
                    .isActive(true)
                    .roles(roles)
                    .loginCount(0)
                    .deviceType(userDTO.getDeviceType())
                    .location(userDTO.getLocation())
                    .signupDate(LocalDate.now())
                    .isTwoFactorEnabled(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);
            log.info("User saved to database:");
            log.info("  - ID: {}", savedUser.getId());
            log.info("  - Password in DB: {}", savedUser.getPassword());
            log.info("  - Password matches original encoded: {}", savedUser.getPassword().equals(encodedPassword));

            // Verify we can retrieve and match
            Optional<User> verifyUser = userRepository.findByEmail(userDTO.getEmail());
            if (verifyUser.isPresent()) {
                boolean verifyMatch = passwordEncoder.matches(plainPassword, verifyUser.get().getPassword());
                log.info("  - Verification: Retrieved from DB and password matches: {}", verifyMatch);

                if (!verifyMatch) {
                    log.error("CRITICAL: Password stored in database doesn't match! Something is wrong with the save process.");
                }
            }

            assignRolesToUser(savedUser, roles);

            log.info("Signup successful for user: {}", savedUser.getUsername());
            log.info("=== SIGNUP ATTEMPT END ===");

            return createUserResponseDTO(String.valueOf(savedUser.getId()), "", "Signup successful", "true");
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage(), e);
            return createUserResponseDTO("", "", "Signup failed: An error occurred", "false");
        }
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UsernameNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private UserResponseDTO createUserResponseDTO(String id, String token, String message, String success) {
        return new UserResponseDTO.Builder()
                .id(id)
                .token(token)
                .message(message)
                .success(success)
                .build();
    }

    public List<UserDetailDTO> getRoles() {
        log.debug("Fetching all roles");
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            log.warn("No roles found");
            return List.of();
        }
        log.debug("Roles found: {}", roles.size());
        return roles.stream()
                .map(role -> new UserDetailDTO.Builder()
                        .name(role.getName())
                        .role(role.getName())
                        .email("")
                        .build())
                .toList();
    }

    public List<UserDetailDTO> getUsersWithRoles() {
        log.debug("Fetching all users with their roles");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.warn("No users found");
            return List.of();
        }
        return users.stream()
                .map(user -> new UserDetailDTO.Builder()
                        .name(user.getUsername())
                        .role(user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse("No Role"))
                        .email(user.getEmail())
                        .build())
                .toList();
    }

    private User updateUserSignInDate(User user) {
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserDetailDTO getUserDetailsById(String userId) {
        log.debug("Fetching user details for id: {}", userId);
        return userRepository.findById(Long.parseLong(userId))
                .map(user -> new UserDetailDTO.Builder()
                        .name(user.getUsername())
                        .role(user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse("No Role"))
                        .email(user.getEmail())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public List<UserDetailDTO> getAllUserDetails() {
        log.debug("Fetching all user details");
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDetailDTO.Builder()
                        .name(user.getUsername())
                        .role(user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse("No Role"))
                        .email(user.getEmail())
                        .build())
                .toList();
    }

    public long countUsers() {
        return userRepository.count();
    }

    /**
     * Generates a cryptographically secure random salt
     * @return Base64 encoded salt string
     */
    private String generateSalt() {
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Assigns all roles to a user
     * @param user The user to assign roles to
     * @param roles The roles to assign
     */
    private void assignRolesToUser(User user, Collection<Role> roles) {
        roles.forEach(role -> {
            UserRoles userRole = new UserRoles.Builder()
                    .userId(user.getId())
                    .roleId(role.getId())
                    .build();
            userRolesRepository.save(userRole);
            log.debug("Assigned role {} to user {}", role.getName(), user.getUsername());
        });
    }
}