package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Calendar;
import com.example.demo.model.ProfileAudit;
import com.example.demo.model.ProfileCalendar;
import com.example.demo.model.ProfileNotification;
import com.example.demo.repository.ProfileAuditRepository;
import com.example.demo.repository.ProfileCalendarRepository;
import com.example.demo.repository.ProfileNotificationRepository;
import com.example.demo.service.impl.*;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseInitializer {

    private final AnnouncementService announcementService;
    private final NotificationService notificationService;
    private final ProfileNotificationRepository profileNotificationRepository;
    private final ProfileCalendarRepository profileCalendarRepository;
    private final AuditService auditService;
    private final ProfileAuditRepository profileAuditRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final ProfileService profileService;
    private final CalendarService calendarService;
    private final StatusService statusService;

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

    public static List<NotificationsDTO> readNotificationsFromFile(String filePath) throws IOException {
        List<NotificationsDTO> notifications = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Pattern pattern = Pattern.compile(
                "NotificationsDTO\\(title = \"(.*?)\", timestamp = \"(.*?)\", message = \"(.*?)\"\\)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String title = matcher.group(1);
                String timestamp = matcher.group(2);
                String message = matcher.group(3);

                notifications.add(new NotificationsDTO(title, timestamp, message));
            }
        }

        return notifications;
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

        if (userService.countUsers() > 0) {
            log.info("Database already initialized, skipping...");
            return null;
        }

        return args -> {
            try {
                initializeRoles();
                initializeStatuses();
                processUsers("/home/yevhen/IdeaProjects/demo/src/main/resources/usersList.txt", passwordEncoder);
                processAnnouncements("/home/yevhen/IdeaProjects/demo/src/main/resources/announcementsList.txt");
                processNotifications("/home/yevhen/IdeaProjects/demo/src/main/resources/notificationsList.txt", 2L);
                processCalendar("/home/yevhen/IdeaProjects/demo/src/main/resources/calendarList.txt", 2L);
                processAudit("/home/yevhen/IdeaProjects/demo/src/main/resources/auditList.txt", 2L);
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

    private void initializeStatuses() {
        statusService.saveStatus(new StatusDTO.Builder()
                .cpu("2.5 GHz")
                .memory("8 GB")
                .apiLatency("100 ms")
                .uptime("24 days, 5 hours")
                .jobs("5 active jobs")
                .build());
    }

    private void processUsers(String filePath, PasswordEncoder passwordEncoder) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            log.error("File not found: {}", filePath);
            return;
        }
        List<UserRequestDTO> users = readUsersFromFile(filePath, passwordEncoder);
        for (UserRequestDTO user : users) {
            user.setDeviceType(generateDeviceType());
            user.setLocation(generateLocation());
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

    private void processNotifications(String filePath, Long profileId) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            log.error("File not found: {}", filePath);
            return;
        }
        List<NotificationsDTO> notifications = readNotificationsFromFile(filePath);
        if (notifications.isEmpty()) {
            log.info("No notifications found in file: {}", filePath);
            return;
        }
        for (NotificationsDTO notification : notifications) {
            // Assuming a method exists to save notifications
            ProfileNotification profileNotification = new ProfileNotification();
            profileNotification.setProfileId(profileId); // Set a valid profile ID
            profileNotification.setNotificationId(notificationService.saveNotification(notification).getId());
            profileNotificationRepository.save(profileNotification);
            log.info("Notification saved successfully: {}", notification);
        }
    }

    private void processCalendar(String filePath, Long profileId) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                log.error("File not found: {}", filePath);
                return;
            }
            List<CalendarDTO> calendars = readCalendarsFromFile(filePath);
            if (calendars.isEmpty()) {
                log.info("No calendars found in file: {}", filePath);
                return;
            }
            for (CalendarDTO calendar : calendars) {
                // Assuming a method exists to save calendars
                Calendar existsCalendar = calendarService.saveCalendar(calendar);
                ProfileCalendar profileCalendar = ProfileCalendar.builder()
                        .profileId(profileId)
                        .calendarId(existsCalendar.getId())
                        .build();
                profileCalendarRepository.save(profileCalendar);
                log.info("Calendar saved successfully: {}", calendar);
            }
        } catch (IOException e) {
            log.error("Error reading calendar file: {}", e.getMessage(), e);
        }
    }

    private void processAudit(String filePath, Long profileId) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                log.error("File not found: {}", filePath);
                return;
            }
            List<AuditDTO> audits = readAuditFromFile(filePath);
            if (audits.isEmpty()) {
                log.info("No audits found in file: {}", filePath);
                return;
            }
            for (AuditDTO audit : audits) {
                // Assuming a method exists to save audits
                auditService.saveAudit(audit);
                ProfileAudit profileAudit = ProfileAudit.builder()
                        .profileId(profileId)
                        .auditId(auditService.saveAudit(audit).getId())
                        .build();
                profileAuditRepository.save(profileAudit);
                log.info("Audit saved successfully: {}", audit);
            }
        } catch (IOException e) {
            log.error("Error reading audit file: {}", e.getMessage(), e);
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