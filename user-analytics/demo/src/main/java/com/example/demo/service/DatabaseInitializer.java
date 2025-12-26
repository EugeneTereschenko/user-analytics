package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Calendar;
import com.example.demo.model.ProfileAudit;
import com.example.demo.model.ProfileCalendar;
import com.example.demo.repository.ProfileAuditRepository;
import com.example.demo.repository.ProfileCalendarRepository;
import com.example.notification.repository.ProfileNotificationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseInitializer {


    private final UserService userService;
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final Environment environment;

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
                String rolesString = matcher.group(4);

                List<String> roles = Arrays.stream(rolesString.split(","))
                        .map(String::trim)
                        .toList();

                users.add(UserRequestDTO.builder()
                        .username(username)
                        .email(email)
                        .password(password)
                        .roles(roles.stream().toList())
                        .build());
            }
        }

        return users;
    }


    @Bean
    public CommandLineRunner initDatabase(PasswordEncoder passwordEncoder) {
        return args -> {

            long userCount = userRepository.count();

            if (userCount > 3) {
                log.info("=================================================");
                log.info("Database already contains {} users - skipping initialization", userCount);
                log.info("=================================================");
                return;
            }

            log.info("=================================================");
            log.info("Database is empty - running initialization...");
            log.info("=================================================");

            try {

                // Use configurable file paths (from application.yml)
                String usersFile = environment.getProperty("app.init.users-file", "usersList.txt");
                if (!usersFile.contains("/") && getClass().getClassLoader().getResource(usersFile) != null) {
                    usersFile = Paths.get(getClass().getClassLoader().getResource(usersFile).toURI()).toString();
                }



                processUsers(usersFile, passwordEncoder);

                log.info("=================================================");
                log.info("Database initialization completed successfully!");
                log.info("=================================================");

            } catch (IOException e) {
                log.error("=================================================");
                log.error("Error initializing database: {}", e.getMessage(), e);
                log.error("=================================================");
            }
        };
    }


    private void processUsers(String filePath, PasswordEncoder passwordEncoder) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            log.warn("Users file not found: {} - skipping user creation", filePath);
            return;
        }

        log.info("Processing users from file: {}", filePath);
        List<UserRequestDTO> users = readUsersFromFile(filePath, passwordEncoder);

        int successCount = 0;
        int failCount = 0;

        for (UserRequestDTO user : users) {
            try {
                user.setDeviceType(generateDeviceType());
                user.setLocation(generateLocation());
                UserResponseDTO response = userService.createUser(user);

                if ("true".equals(response.getSuccess())) {
                    profileService.createProfile(Long.valueOf(response.getId()));
                    successCount++;
                    log.debug("User created successfully: {}", user.getUsername());
                } else {
                    failCount++;
                    log.error("User creation failed: {} - {}", user.getUsername(), response.getMessage());
                }
            } catch (Exception e) {
                failCount++;
                log.error("Error creating user: {} - {}", user.getUsername(), e.getMessage());
            }
        }

        log.info("Users processed: {} successful, {} failed", successCount, failCount);
    }

    private String generateDeviceType() {
        List<String> deviceTypes = List.of("Desktop", "Mobile", "Tablet", "Smartwatch", "SmartTV");
        return deviceTypes.get(new Random().nextInt(deviceTypes.size()));
    }

    private String generateLocation() {
        List<String> location = List.of("USA", "India", "Germany", "Ukraine", "Canada", "UK", "France", "Italy", "Spain", "Australia");
        return location.get(new Random().nextInt(location.size()));
    }
}