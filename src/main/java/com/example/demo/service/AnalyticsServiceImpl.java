package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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


}
