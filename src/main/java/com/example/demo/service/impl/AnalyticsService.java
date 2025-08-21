package com.example.demo.service.impl;

public interface AnalyticsService {
    long countNewUsersToday();
    String getMostActiveUser();
    String getWeeklyStats();
}
