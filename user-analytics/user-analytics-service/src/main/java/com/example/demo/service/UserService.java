package com.example.demo.service;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.service.ActivityService;
import com.example.demo.dto.UserDetailDTO;
import com.example.demo.dto.UserEventDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.activity.model.ActivityType;
import com.example.demo.event.UserEventPublisher;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ProfileRepository profileRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final UserRolesRepository userRolesRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ActivityService activityService; // ADD THIS
    private final HttpServletRequest request; // ADD THIS for IP tracking
    private final UserEventPublisher userEventPublisher;

    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        log.info("=== SIGNUP ATTEMPT START ===");
        log.info("Email: {}", userDTO.getEmail());
        log.info("Username: {}", userDTO.getUsername());

        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                logActivity(null, userDTO.getUsername(), ActivityType.CREATE,
                        "Failed signup attempt - email already exists");
                return createUserResponseDTO("", "", "Signup failed: Email already exists", "false");
            }

            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                logActivity(null, userDTO.getUsername(), ActivityType.CREATE,
                        "Failed signup attempt - username already exists");
                return createUserResponseDTO("", "", "Signup failed: Username already exists", "false");
            }

            Collection<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            if (roles.isEmpty()) {
                return createUserResponseDTO("", "", "Signup failed: Invalid roles", "false");
            }

            String plainPassword = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(plainPassword);
            String salt = generateSalt();

            User user = new User.Builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .password(encodedPassword)
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
            assignRolesToUser(savedUser, roles);

            // Log successful signup
            logActivity(savedUser.getId(), savedUser.getUsername(), ActivityType.CREATE,
                    "New user account created");

            // *** PUBLISH EVENT TO KAFKA ***
            publishUserCreatedEvent(savedUser);

            log.info("Signup successful for user: {}", savedUser.getUsername());
            log.info("=== SIGNUP ATTEMPT END ===");

            getOrCreateUserProfile(savedUser.getId());
            return createUserResponseDTO(String.valueOf(savedUser.getId()), "", "Signup successful", "true");
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage(), e);
            return createUserResponseDTO("", "", "Signup failed: An error occurred", "false");
        }
    }

    public UserResponseDTO loginUser(UserRequestDTO userDTO) {
        log.info("=== LOGIN ATTEMPT START ===");
        log.info("Email: {}", userDTO.getEmail());

        try {
            Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
            if (userOptional.isEmpty()) {
                log.warn("Login attempt for non-existent email");
                logActivity(null, null, ActivityType.LOGIN,
                        "Failed login attempt for email: " + userDTO.getEmail());
                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            User user = userOptional.get();
            log.info("User found in DB: {}", user.getUsername());

            String plainPassword = userDTO.getPassword();
            String storedHash = user.getPassword();

            boolean matches = passwordEncoder.matches(plainPassword, storedHash);

            if (!matches) {
                log.warn("Password mismatch for user: {}", user.getUsername());
                logActivity(user.getId(), user.getUsername(), ActivityType.LOGIN,
                        "Failed login attempt - incorrect password");
                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            log.info("Password matched successfully!");

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            if (userDetails == null) {
                return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
            }

            String token = jwtUtil.generateToken(userDetails.getUsername());
            updateUserSignInDate(user);

            // Log successful login
            logActivity(user.getId(), user.getUsername(), ActivityType.LOGIN,
                    "User logged in successfully");

            // *** PUBLISH LOGIN EVENT TO KAFKA ***
            publishLoginEvent(user);

            log.info("Login successful for user: {}", user.getUsername());
            log.info("=== LOGIN ATTEMPT END ===");

            return createUserResponseDTO(String.valueOf(user.getId()), token, "Login successful", "true");
        } catch (Exception e) {
            log.error("Failed to login: {}", e.getMessage(), e);
            return createUserResponseDTO("", "", "Login failed: Invalid credentials", "false");
        }
    }

    // Event publishing methods
    private void publishUserCreatedEvent(User user) {
        try {
            UserEventDTO event = UserEventDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .timestamp(LocalDateTime.now())
                    .roles(user.getRoles().stream().map(Role::getName).toList())
                    .location(user.getLocation())
                    .deviceType(user.getDeviceType())
                    .isActive(user.isActive())
                    .build();

            userEventPublisher.publishUserCreated(event);
        } catch (Exception e) {
            log.error("Failed to publish user created event: {}", e.getMessage());
        }
    }

    private void publishLoginEvent(User user) {
        try {
            UserEventDTO event = UserEventDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .timestamp(LocalDateTime.now())
                    .roles(user.getRoles().stream().map(Role::getName).toList())
                    .location(user.getLocation())
                    .deviceType(user.getDeviceType())
                    .isActive(user.isActive())
                    .build();

            userEventPublisher.publishUserLogin(event);
        } catch (Exception e) {
            log.error("Failed to publish login event: {}", e.getMessage());
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

    @Transactional
    private Profile getOrCreateUserProfile(Long userId) {

        log.debug("Creating profile for user ID: {}", userId);
        return profileRepository.findLatestProfileByUserId(userId)
                .orElseGet(() -> createProfile(userId));
    }

    @Transactional
    public Profile createProfile(Long userId) {
        Profile profile = profileRepository.saveAndFlush(new Profile());

        UserProfile userProfile = userProfileRepository.saveAndFlush(
                new UserProfile.Builder()
                        .userId(userId)
                        .profileId(profile.getId())
                        .build()
        );

        log.info("Profile created successfully with ID: {} for user ID: {}", profile.getId(), userId);
        return profile;
    }

    // Helper method to log activities
    private void logActivity(Long userId, String username, ActivityType type, String description) {
        try {
            ActivityDTO activityDTO = new ActivityDTO.Builder()
                    .userId(userId)
                    .username(username != null ? username : "anonymous")
                    .type(type)
                    .description(description)
                    .ipAddress(getClientIp())
                    .deviceType(getUserAgent())
                    .location("Unknown") // You can integrate with a geolocation service
                    .build();

            activityService.logActivity(activityDTO);
        } catch (Exception e) {
            log.error("Failed to log activity: {}", e.getMessage());
            // Don't fail the main operation if activity logging fails
        }
    }

    // Get client IP address
    private String getClientIp() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    // Get user agent (device/browser info)
    private String getUserAgent() {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown";
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

        // Log view activity
        User currentUser = getAuthenticatedUser().orElse(null);
        if (currentUser != null) {
            logActivity(currentUser.getId(), currentUser.getUsername(), ActivityType.VIEW,
                    "Viewed user details for user ID: " + userId);
        }

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

        // Log view activity
        User currentUser = getAuthenticatedUser().orElse(null);
        if (currentUser != null) {
            logActivity(currentUser.getId(), currentUser.getUsername(), ActivityType.VIEW,
                    "Viewed all user details");
        }

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

    private String generateSalt() {
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

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