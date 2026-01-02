package com.example.demo.service.impl;

import com.example.demo.dto.UserSummaryDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    int countNewUsersToday();
    String getWeeklyStats();
    String getMostActiveUser();
    int getTotalUsers();
    Map<String, Object> getUserGrowthData();
    List<String> getTopActiveUsers(int limit);
    UserSummaryDTO getUserSummary(LocalDate start, LocalDate end);
    int[] getSignups(LocalDate start, LocalDate end);
    Map<String, Integer> getDeviceBreakdown();
    Map<String, Integer> getUserLocations();
}
