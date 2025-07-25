package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseInitializer {

    private final RoleService roleService;
    private final UserService userService;
    private final ProfileService profileService;

    public static List<UserRequestDTO> readUsersFromFile(String filePath, PasswordEncoder passwordEncoder) throws IOException {
        List<UserRequestDTO> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

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


            String filePath = "/home/yevhen/IdeaProjects/demo/src/main/resources/usersList.txt";
            try {
                if (!Files.exists(Paths.get(filePath))) {
                    log.error("File not found: {}", filePath);
                    return;
                }
                List<UserRequestDTO> users = readUsersFromFile(filePath, passwordEncoder);
                for (UserRequestDTO user : users) {
                    UserResponseDTO response = userService.createUser(user);
                    profileService.createProfile(Long.valueOf(response.getId()));
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