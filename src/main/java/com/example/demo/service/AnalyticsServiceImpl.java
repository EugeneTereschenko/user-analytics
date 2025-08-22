package com.example.demo.service;

import com.example.demo.dto.UserSummaryDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;

    // Example mock data (replace with repository queries later)
    private final Map<LocalDate, Long> userSignups = Map.of(
            LocalDate.now(), 15L,
            LocalDate.now().minusDays(1), 20L
    );

    @Override
    public long countNewUsersToday() {
        return userRepository.countBySignupDate(LocalDate.now());
    }

    @Override
    public String getMostActiveUser() {
        return userRepository.findMostActiveUser()
                .map(User::getUsername)
                .orElse("No active users found");
    }

    public long countSignupsInLastDays(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return userRepository.countSignupsSince(startDate);
    }

    public long countActiveUsersInLastDays(int days) {
        LocalDateTime startDateTime = LocalDate.now().minusDays(days).atStartOfDay();
        return userRepository.countActiveUsersSince(startDateTime);
    }

    @Override
    public String getWeeklyStats() {
        long weeklySignups = countSignupsInLastDays(7);
        long weeklyActive = countActiveUsersInLastDays(7);
        return "This week: " + weeklySignups + " signups, " + weeklyActive + " active users.";
    }

    public UserSummaryDTO getUserSummary(LocalDate start, LocalDate end) {
        UserSummaryDTO userSummaryDTO = userRepository.getUserSummary(start, end);
        log.info("UserSummaryDTO log: " + userSummaryDTO.toString());
        return userSummaryDTO;
    }

    public int[] getSignups(LocalDate start, LocalDate end) {
        // Mock implementation
        return new int[]{10, 20, 15, 30, 25};
    }

    public Map<String, Integer> getDeviceBreakdown() {
        // Mock implementation
        return Map.of("Desktop", 5000, "Mobile", 7000, "Tablet", 1500);
    }

    public Map<String, Integer> getUserLocations() {
        // Mock implementation
        return Map.of("USA", 4000, "India", 3000, "Germany", 1000);
    }


}
