package com.example.assistant.service;


import com.example.assistant.service.impl.Intent;
import com.example.demo.service.impl.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveUserIntent implements Intent {

    private final AnalyticsService analyticsService;

    @Override
    public boolean matches(String input) {
        return input.contains("active user") || input.contains("most active");
    }

    @Override
    public String handle(String input) {
        String activeUser = analyticsService.getMostActiveUser();
        return "The most active user is: " + activeUser;
    }

    @Override
    public String getDescription() {
        return "Shows the most active user";
    }
}
