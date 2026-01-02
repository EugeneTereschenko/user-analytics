package com.example.assistant.service;


import com.example.assistant.service.impl.Command;
import com.example.demo.service.impl.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersTodayCommand implements Command {

    private final AnalyticsService analyticsService;

    @Override
    public String getCommandName() {
        return "/users";
    }

    @Override
    public String getDescription() {
        return "Shows today's new user count (usage: /users today)";
    }

    @Override
    public String execute(String[] args) {
        if (args.length > 0 && args[0].equals("today")) {
            int count = analyticsService.countNewUsersToday();
            return String.format("Today we had %d new %s.",
                    count, count == 1 ? "user" : "users");
        }
        return "Usage: /users today";
    }
}
