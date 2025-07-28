package com.example.demo.service;

import com.example.demo.dto.AnnouncementDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.impl.AnnouncementService;
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

    private final AnnouncementService announcementService;
    private final RoleService roleService;
    private final UserService userService;
    private final ProfileService profileService;

    public static List<AnnouncementDTO> readAnnouncementsFromFile(String filePath) throws IOException {
        List<AnnouncementDTO> announcements = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Pattern pattern = Pattern.compile(
                "AnnouncementDTO\\(id\\s*=\\s*(\\d+),\\s*title\\s*=\\s*\"(.*?)\",\\s*content\\s*=\\s*\"(.*?)\",\\s*date\\s*=\\s*\"(.*?)\"\\)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group(1));
                String title = matcher.group(2);
                String content = matcher.group(3);
                String date = matcher.group(4);

                announcements.add(AnnouncementDTO.builder()
                        .id(id)
                        .title(title)
                        .body(content)
                        .date(date)
                        .build());
            } else {
                System.out.println("No match for line: " + line);  // Debugging
            }
        }

        return announcements;
    }


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
            try {
                initializeRoles();
                processUsers("/home/yevhen/IdeaProjects/demo/src/main/resources/usersList.txt", passwordEncoder);
                processAnnouncements("/home/yevhen/IdeaProjects/demo/src/main/resources/announcementsList.txt");
            } catch (IOException e) {
                log.error("Error initializing database: {}", e.getMessage(), e);
            }
        };
    }

    private void initializeRoles() {
        roleService.createRole("ROLE_USER");
        roleService.createRole("ROLE_ADMIN");
        roleService.createRole("ROLE_EDITOR");
        roleService.createRole("ROLE_VIEWER");
    }

    private void processUsers(String filePath, PasswordEncoder passwordEncoder) throws IOException {
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
    }

    private void processAnnouncements(String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            log.error("File not found: {}", filePath);
            return;
        }
        List<AnnouncementDTO> announcements = readAnnouncementsFromFile(filePath);
        if (announcements.isEmpty()) {
            log.info("No announcements found in file: {}", filePath);
            return;
        }
        for (AnnouncementDTO announcement : announcements) {
            announcementService.saveAnnouncement(announcement);
            log.info("Announcement saved successfully: {}", announcement);
        }
    }

}