package com.example.demo.service.impl;

import com.example.demo.dto.UserSummaryDTO;

import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {
    long countNewUsersToday();
    String getMostActiveUser();
    String getWeeklyStats();
    UserSummaryDTO getUserSummary(LocalDate start, LocalDate end);
    int[] getSignups(LocalDate start, LocalDate end);
    Map<String, Integer> getDeviceBreakdown();
    Map<String, Integer> getUserLocations();
}
