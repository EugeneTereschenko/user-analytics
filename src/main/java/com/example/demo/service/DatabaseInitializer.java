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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseInitializer {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;

    public static List<UserRequestDTO> readUsersFromFile(String filePath, PasswordEncoder passwordEncoder) throws IOException {
        List<UserRequestDTO> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // Pattern: extract values from text like: UserRequestDTO(username=john_doe, email=john.doe@example.com, password=pass123, roles=[ROLE_USER])
        Pattern pattern = Pattern.compile(
                "UserRequestDTO\\(username=(.*?), email=(.*?), password=(.*?), roles=\\[(.*?)\\]\\)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String username = matcher.group(1);
                String email = matcher.group(2);
                String password = matcher.group(3);
                String rolesString = matcher.group(4); // comma-separated roles

                List<String> roles = Arrays.stream(rolesString.split(","))
                        .map(String::trim)
                        .toList();

                users.add(UserRequestDTO.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .roles(roles.stream().toList())
                        .build());
            }
        }

        return users;
    }

    @Bean
    public CommandLineRunner initDatabase(PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize roles
            roleService.createRole("ROLE_USER");
            roleService.createRole("ROLE_ADMIN");
            roleService.createRole("ROLE_EDITOR");
            roleService.createRole("ROLE_VIEWER");

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


            String filePath = "/home/yevhen/IdeaProjects/demo/src/main/resources/usersList.txt";
            try {
                if (!Files.exists(Paths.get(filePath))) {
                    log.error("File not found: {}", filePath);
                    return;
                }
                List<UserRequestDTO> users = readUsersFromFile(filePath, passwordEncoder);
                for (UserRequestDTO user : users) {
                    UserResponseDTO response = userService.createUser(user);
                    if (!response.getSuccess().equals("true")) {
                        log.error("User not created: {}", response.getMessage());
                    } else {
                        log.info("User created successfully: {}", response.getToken());
                    }
                }
                log.info("Users initialized from file: {}", filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}