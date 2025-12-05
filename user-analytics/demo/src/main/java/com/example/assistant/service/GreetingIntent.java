package com.example.assistant.service;


import com.example.assistant.service.impl.Intent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GreetingIntent implements Intent {

    private static final List<String> GREETINGS = List.of(
            "hello", "hi", "hey", "greetings", "good morning",
            "good afternoon", "good evening"
    );

    private static final List<String> RESPONSES = List.of(
            "Hi there! How can I assist you with analytics today?",
            "Hello! I'm here to help with your user analytics.",
            "Hey! What would you like to know about your users?",
            "Greetings! Ready to dive into your analytics?"
    );

    @Override
    public boolean matches(String input) {
        return GREETINGS.stream()
                .anyMatch(greeting -> input.contains(greeting));
    }

    @Override
    public String handle(String input) {
        return RESPONSES.get(new Random().nextInt(RESPONSES.size()));
    }

    @Override
    public String getDescription() {
        return "Responds to greetings and welcomes the user";
    }
}
