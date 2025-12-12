package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Calendar;
import com.example.demo.model.ProfileAudit;
import com.example.demo.model.ProfileCalendar;
import com.example.demo.model.ProfileNotification;
import com.example.demo.repository.ProfileAuditRepository;
import com.example.demo.repository.ProfileCalendarRepository;
import com.example.demo.repository.ProfileNotificationRepository;
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

    private final ProfileNotificationRepository profileNotificationRepository;
    private final ProfileCalendarRepository profileCalendarRepository;
    private final AuditService auditService;
    private final ProfileAuditRepository profileAuditRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository; // Add this
    private final ProfileService profileService;
    private final CalendarService calendarService;
    private final StatusService statusService;
    private final Environment environment; // Add this

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

    public static List<CalendarDTO> readCalendarsFromFile(String filePath) throws IOException {
        List<CalendarDTO> calendars = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Pattern pattern = Pattern.compile(
                "CalendarDTO\\(title = \"(.*?)\", date = \"(.*?)\"\\)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String title = matcher.group(1);
                String date = matcher.group(2);

                calendars.add(CalendarDTO.builder()
                        .title(title)
                        .date(date)
                        .build());
            }
        }

        return calendars;
    }

    public static List<AuditDTO> readAuditFromFile(String filePath) throws IOException {
        List<AuditDTO> audits = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Pattern pattern = Pattern.compile(
                "AuditDTO\\(user = \"(.*?)\", action = \"(.*?)\", target = \"(.*?)\", timestamp = \"(.*?)\"\\)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String user = matcher.group(1);
                String action = matcher.group(2);
                String target = matcher.group(3);
                String timestamp = matcher.group(4);

                audits.add(new AuditDTO.Builder()
                        .user(user)
                        .action(action)
                        .target(target)
                        .timestamp(timestamp)
                        .build());
            }
        }

        return audits;
    }

    @Bean
    public CommandLineRunner initDatabase(PasswordEncoder passwordEncoder) {
        return args -> {

            long userCount = userRepository.count();

            if (userCount > 0) {
                log.info("=================================================");
                log.info("Database already contains {} users - skipping initialization", userCount);
                log.info("=================================================");
                return;
            }

            log.info("=================================================");
            log.info("Database is empty - running initialization...");
            log.info("=================================================");

            try {
                initializeRoles();
                initializeStatuses();

                // Use configurable file paths (from application.yml)
                String usersFile = environment.getProperty(
                        "app.init.users-file",
                        "/home/yevhen/IdeaProjects/demo/user-analytics/demo/src/main/resources/usersList.txt"
                );
                String calendarFile = environment.getProperty(
                        "app.init.calendar-file",
                        "/home/yevhen/IdeaProjects/demo/user-analytics/demo/src/main/resources/calendarList.txt"
                );
                String auditFile = environment.getProperty(
                        "app.init.audit-file",
                        "/home/yevhen/IdeaProjects/demo/user-analytics/demo/src/main/resources/auditList.txt"
                );

                processUsers(usersFile, passwordEncoder);

                if (userRepository.count() > 0) {
                    Long defaultProfileId = 2L; // Or get dynamically
                    processCalendar(calendarFile, defaultProfileId);
                    processAudit(auditFile, defaultProfileId);
                }

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

    private void initializeRoles() {
        log.info("Initializing roles...");
        roleService.createRole("ROLE_USER");
        roleService.createRole("ROLE_ADMIN");
        roleService.createRole("ROLE_EDITOR");
        roleService.createRole("ROLE_VIEWER");
        log.info("Roles initialized successfully");
    }

    private void initializeStatuses() {
        log.info("Initializing system status...");
        statusService.saveStatus(new StatusDTO.Builder()
                .cpu("2.5 GHz")
                .memory("8 GB")
                .apiLatency("100 ms")
                .uptime("24 days, 5 hours")
                .jobs("5 active jobs")
                .build());
        log.info("System status initialized successfully");
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

    private void processCalendar(String filePath, Long profileId) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                log.warn("Calendar file not found: {} - skipping", filePath);
                return;
            }

            log.info("Processing calendar entries from file: {}", filePath);
            List<CalendarDTO> calendars = readCalendarsFromFile(filePath);

            if (calendars.isEmpty()) {
                log.info("No calendar entries found in file");
                return;
            }

            int count = 0;
            for (CalendarDTO calendar : calendars) {
                try {
                    Calendar existsCalendar = calendarService.saveCalendar(calendar);
                    ProfileCalendar profileCalendar = ProfileCalendar.builder()
                            .profileId(profileId)
                            .calendarId(existsCalendar.getId())
                            .build();
                    profileCalendarRepository.save(profileCalendar);
                    count++;
                } catch (Exception e) {
                    log.error("Error saving calendar entry: {} - {}", calendar.getTitle(), e.getMessage());
                }
            }

            log.info("Calendar entries processed: {} saved successfully", count);

        } catch (IOException e) {
            log.error("Error reading calendar file: {}", e.getMessage());
        }
    }

    private void processAudit(String filePath, Long profileId) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                log.warn("Audit file not found: {} - skipping", filePath);
                return;
            }

            log.info("Processing audit entries from file: {}", filePath);
            List<AuditDTO> audits = readAuditFromFile(filePath);

            if (audits.isEmpty()) {
                log.info("No audit entries found in file");
                return;
            }

            int count = 0;
            for (AuditDTO audit : audits) {
                try {
                    auditService.saveAudit(audit);
                    ProfileAudit profileAudit = ProfileAudit.builder()
                            .profileId(profileId)
                            .auditId(auditService.saveAudit(audit).getId())
                            .build();
                    profileAuditRepository.save(profileAudit);
                    count++;
                } catch (Exception e) {
                    log.error("Error saving audit entry: {} - {}", audit.getUser(), e.getMessage());
                }
            }

            log.info("Audit entries processed: {} saved successfully", count);

        } catch (IOException e) {
            log.error("Error reading audit file: {}", e.getMessage());
        }
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