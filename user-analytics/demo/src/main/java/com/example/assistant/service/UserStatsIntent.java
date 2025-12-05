package com.example.assistant.service;

import com.example.assistant.service.impl.Intent;
import com.example.demo.service.impl.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserStatsIntent implements Intent {

    private final AnalyticsService analyticsService;

    private static final Pattern USERS_TODAY_PATTERN =
            Pattern.compile(".*(users?|signups?|registrations?)\\s+(today|now).*");

    @Override
    public boolean matches(String input) {
        return USERS_TODAY_PATTERN.matcher(input).matches();
    }

    @Override
    public String handle(String input) {
        int count = analyticsService.countNewUsersToday();
        return String.format("Today %d %s signed up.",
                count, count == 1 ? "user" : "users");
    }

    @Override
    public String getDescription() {
        return "Provides today's user signup statistics";
    }
}
