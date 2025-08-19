package com.example.demo.service;

import com.example.demo.service.impl.AnalyticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    // Example mock data (replace with repository queries later)
    private final Map<LocalDate, Long> userSignups = Map.of(
            LocalDate.now(), 15L,
            LocalDate.now().minusDays(1), 20L
    );

    @Override
    public long countNewUsersToday() {
        return userSignups.getOrDefault(LocalDate.now(), 0L);
    }

    @Override
    public String getMostActiveUser() {
        // Replace with query like: SELECT username FROM users ORDER BY login_count DESC LIMIT 1
        return "john_doe"; // mocked
    }

    @Override
    public String getWeeklyStats() {
        // Replace with aggregated query
        long totalSignups = userSignups.values().stream().mapToLong(Long::longValue).sum();
        return "This week: " + totalSignups + " signups, 3 most active users: john_doe, alice, bob.";
    }


}
