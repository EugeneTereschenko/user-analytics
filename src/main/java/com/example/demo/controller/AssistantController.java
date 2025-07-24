package com.example.demo.controller;


import com.example.demo.dto.AssistantDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody AssistantDTO assistantDTO) {
        log.info("Received message from assistant: {}", assistantDTO);
        String incomingText = assistantDTO.getText();
        if (incomingText == null || incomingText.isBlank()) {
            log.warn("Received null or empty message from assistant");
            return ResponseEntity.badRequest().body(Map.of("error", "Message text cannot be null or empty"));
        }

        log.info("Received message from assistant: {}", incomingText);

        String reply;
        incomingText = incomingText.toLowerCase();

        // Simple keyword-based response logic
        if (incomingText.contains("hello") || incomingText.contains("hi")) {
            reply = "Hi there! How can I assist you today?";
        } else if (incomingText.contains("weather")) {
            reply = "I'm not sure about the weather now, but I can check if you integrate a weather API.";
        } else {
            reply = "I'm not sure how to respond to that yet, but I'm learning!";
        }

        Map<String, String> response = new HashMap<>();
        response.put("text", reply);
        return ResponseEntity.ok(response);
    }
}


