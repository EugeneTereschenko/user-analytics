package com.example.demo.service;

import com.example.demo.dto.AssistantDTO;
import com.example.demo.service.impl.AnalyticsService;
import com.example.demo.service.impl.AssistantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class AssistantServiceImpl implements AssistantService {

    private final AnalyticsService analyticsService;

    @Override
    public Map<String, String> proceedWithAssistant(AssistantDTO assistantDTO) {
        String incomingText = assistantDTO.getText();

        if (incomingText == null || incomingText.isBlank()) {
            log.warn("Received null or empty message from assistant");
            return Map.of("error", "Message text cannot be null or empty");
        }

        log.info("Received message from assistant: {}", incomingText);

        String reply;
        incomingText = incomingText.toLowerCase().trim();

        // Check for command-like messages first
        if (incomingText.startsWith("/")) {
            reply = handleCommand(incomingText);
        } else {
            reply = handleIntent(incomingText);
        }

        return Map.of("text", reply);
    }

    // Example: command handler (/users, /stats, /help, etc.)
    private String handleCommand(String command) {
        return switch (command) {
            case "/help" -> "Available commands: /users today, /stats weekly, /active user";
            case "/users today" -> "Today we had " + analyticsService.countNewUsersToday() + " new users.";
            case "/stats weekly" -> analyticsService.getWeeklyStats(); // e.g. "This week: 120 signups, 80 active users"
            case "/active user" -> "Most active user is: " + analyticsService.getMostActiveUser();
            default -> "Unknown command. Type /help to see available options.";
        };
    }

    // Example: natural-language intent detection
    private String handleIntent(String text) {
        if (text.contains("hello") || text.contains("hi")) {
            return "Hi there! How can I assist you with analytics today?";
        } else if (text.contains("how are you")) {
            return "I'm doing great! By the way, your app had "
                    + analyticsService.countNewUsersToday() + " new users today.";
        } else if (text.matches(".*(users?|signups?) today.*")) {
            return "Today " + analyticsService.countNewUsersToday() + " users signed up.";
        } else if (text.contains("active user")) {
            return "The most active user is: " + analyticsService.getMostActiveUser();
        } else if (text.contains("stats") || text.contains("report")) {
            return analyticsService.getWeeklyStats();
        }

        return "I'm not sure how to respond to that yet. Try asking about users, stats, or activity!";
    }


}
