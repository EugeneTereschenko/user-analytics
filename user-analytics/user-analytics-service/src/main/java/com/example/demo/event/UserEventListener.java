package com.example.demo.event;

import com.example.demo.dto.UserEventDTO;
import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @KafkaListener(topics = "user-events", groupId = "analytics-service")
    @Transactional
    public void handleUserEvent(UserEventDTO event) {
        log.info("=== RECEIVED KAFKA EVENT ===");
        log.info("Event Type: {}", event.getEventType());
        log.info("User: {}", event.getUsername());
        log.info("Email: {}", event.getEmail());

        try {
            switch (event.getEventType()) {
                case "USER_CREATED" -> handleUserCreated(event);
                case "USER_UPDATED" -> handleUserUpdated(event);
                case "USER_LOGIN" -> handleUserLogin(event);
                default -> log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing user event: {}", e.getMessage(), e);
            // You might want to send this to a dead letter queue
        }
    }

    private void handleUserCreated(UserEventDTO event) {
        log.info("Processing USER_CREATED event for: {}", event.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(event.getUsername());

        if (existingUser.isPresent()) {
            log.info("User already exists in analytics DB: {}", event.getUsername());
            return;
        }

        // Create new user replica in analytics DB
        User user = new User.Builder()
                // .userId(event.getUserId()) // Do NOT set ID manually, let DB generate it to avoid conflicts
                .username(event.getUsername())
                .email(event.getEmail())
                .salt("DUMMY_SALT") // Required field but not needed for analytics
                .password("DUMMY_PASSWORD") // Required field but not needed for analytics
                .isActive(event.getIsActive() != null ? event.getIsActive() : true)
                .location(event.getLocation())
                .deviceType(event.getDeviceType())
                .signupDate(event.getTimestamp().toLocalDate())
                .createdAt(event.getTimestamp())
                .loginCount(0)
                .activityCount(0)
                .roles(event.getRoles() != null ? event.getRoles().stream()
                        .map(roleName -> roleRepository.findByName(roleName)
                                .orElseGet(() -> createRole(roleName)))
                        .collect(Collectors.toList()) : null)
                .build();

        userRepository.save(user);
        log.info("✅ User replicated to analytics DB: {} (ID: {})", event.getUsername(), event.getUserId());
    }

    private void handleUserUpdated(UserEventDTO event) {
        log.info("Processing USER_UPDATED event for: {}", event.getUsername());

        Optional<User> userOptional = userRepository.findByUsername(event.getUsername());

        if (userOptional.isEmpty()) {
            log.warn("User not found for update: {}, creating new user", event.getUsername());
            handleUserCreated(event);
            return;
        }

        User user = userOptional.get();
        // ... rest of method
        user.setUsername(event.getUsername());
        user.setEmail(event.getEmail());
        user.setLocation(event.getLocation());
        user.setDeviceType(event.getDeviceType());
        user.setActive(event.getIsActive());
        user.setUpdatedAt(event.getTimestamp());

        userRepository.save(user);
        log.info("✅ User updated in analytics DB: {}", event.getUsername());
    }

    private void handleUserLogin(UserEventDTO event) {
        log.info("Processing USER_LOGIN event for: {}", event.getUsername());

        Optional<User> userOptional = userRepository.findByUsername(event.getUsername());

        if (userOptional.isEmpty()) {
            log.warn("User not found for login tracking: {}, creating user", event.getUsername());
            handleUserCreated(event);
            return;
        }

        User user = userOptional.get();
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLogin(event.getTimestamp());
        user.setLastLoginAt(event.getTimestamp());
        user.setActivityCount(user.getActivityCount() != null ? user.getActivityCount() + 1 : 1);

        userRepository.save(user);
        log.info("✅ Login tracked for user: {} (Total logins: {})",
                event.getUsername(), user.getLoginCount());
    }

    private Role createRole(String roleName) {
        log.info("Creating new role in analytics DB: {}", roleName);
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}