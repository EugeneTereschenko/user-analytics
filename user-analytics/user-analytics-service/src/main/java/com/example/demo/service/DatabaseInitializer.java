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


    private List<UserRequestDTO> getPredefinedUsers() {
        return List.of(
                UserRequestDTO.builder().username("admin").email("test@test.com").password("admin123").roles(List.of("ROLE_ADMIN")).build(),
                UserRequestDTO.builder().username("wer").email("wer@test.com").password("asdasd").roles(List.of("ROLE_USER")).build(),
                UserRequestDTO.builder().username("john_doe").email("john.doe@example.com").password("pass123").roles(List.of("ROLE_USER")).build(),
                UserRequestDTO.builder().username("admin_jane").email("jane.admin@example.com").password("adminpass").roles(List.of("ROLE_ADMIN")).build(),
                UserRequestDTO.builder().username("editor_bob").email("bob.editor@example.com").password("editme").roles(List.of("ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("viewer_sue").email("sue.viewer@example.com").password("seeall").roles(List.of("ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("multi_role_mike").email("mike.multi@example.com").password("multi123").roles(List.of("ROLE_USER", "ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("full_access_amy").email("amy.all@example.com").password("rootpass").roles(List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_EDITOR", "ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("user_mary").email("mary.user@example.com").password("user123").roles(List.of("ROLE_USER")).build(),
                UserRequestDTO.builder().username("admin_ben").email("ben.admin@example.com").password("benpass").roles(List.of("ROLE_ADMIN", "ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("team_lead_alex").email("alex.lead@example.com").password("leadpass").roles(List.of("ROLE_ADMIN", "ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("guest_ken").email("ken.guest@example.com").password("guest123").roles(List.of("ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("support_lisa").email("lisa.support@example.com").password("supportme").roles(List.of("ROLE_USER", "ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("qa_nick").email("nick.qa@example.com").password("test123").roles(List.of("ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("dev_sam").email("sam.dev@example.com").password("devpass").roles(List.of("ROLE_USER", "ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("ops_ray").email("ray.ops@example.com").password("ops123").roles(List.of("ROLE_ADMIN")).build(),
                UserRequestDTO.builder().username("mod_danny").email("danny.mod@example.com").password("mod456").roles(List.of("ROLE_EDITOR", "ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("auditor_kim").email("kim.audit@example.com").password("audit789").roles(List.of("ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("analyst_tina").email("tina.analyst@example.com").password("analyze123").roles(List.of("ROLE_USER", "ROLE_VIEWER")).build(),
                UserRequestDTO.builder().username("trainer_lee").email("lee.trainer@example.com").password("teachme").roles(List.of("ROLE_EDITOR")).build(),
                UserRequestDTO.builder().username("guest_felix").email("felix.guest@example.com").password("temporary").roles(List.of("ROLE_USER")).build(),
                UserRequestDTO.builder().username("admin_ceo").email("ceo.admin@example.com").password("ceopower").roles(List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_EDITOR")).build()
        );
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

                List<UserRequestDTO> users = getPredefinedUsers();
                processUsers(users);

                log.info("=================================================");
                log.info("Database initialization completed successfully!");
                log.info("=================================================");

            } catch (Exception e) {
                log.error("=================================================");
                log.error("Error initializing database: {}", e.getMessage(), e);
                log.error("=================================================");
            }
        };
    }


    private void processUsers(List<UserRequestDTO> users) {
        int failCount = 0;
        for (UserRequestDTO user : users) {
            try {
                user.setDeviceType(generateDeviceType());
                user.setLocation(generateLocation());
                userService.createUser(user);
            } catch (Exception e) {
                failCount++;
                log.error("Error creating user: {} - {}", user.getUsername(), e.getMessage());
            }
        }
        log.info("Users processed: {} failed", failCount);
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