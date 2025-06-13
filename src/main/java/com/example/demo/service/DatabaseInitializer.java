package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseInitializer {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initDatabase(PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize roles
            roleService.createRole("ROLE_USER");
            roleService.createRole("ROLE_ADMIN");

            // Initialize users
            UserRequestDTO adminUser = UserRequestDTO.builder()
                    .username("admin")
                    .email("test@test.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(java.util.Collections.singletonList("ROLE_ADMIN"))
                    .build();

            try {
                UserResponseDTO response = userService.createUser(adminUser);
                if (!response.getSuccess().equals("true")) {
                    log.error("User not created: {}", response.getMessage());
                } else {
                    log.info("User created successfully: {}", response.getToken());
                }
                Optional<User> userExists = userRepository.findByUsername("admin");
                if (userExists.isPresent()) {
                    log.info("User already exists: {}", userExists.get().getUsername());
                } else {
                    log.info("User does not exist, creating new user.");
                }
            } catch (Exception e) {
                log.error("Error creating user: {}", e.getMessage());
            }

            UserRequestDTO userUser = UserRequestDTO.builder()
                    .username("wer")
                    .email("wer@test.com")
                    .password(passwordEncoder.encode("asdasd"))
                    .roles(java.util.Collections.singletonList("ROLE_USER"))
                    .build();
            userService.createUser(userUser);
        };
    }
}