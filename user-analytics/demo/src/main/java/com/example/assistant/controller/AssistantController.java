package com.example.assistant.controller;

import com.example.assistant.dto.AssistantDTO;
import com.example.assistant.dto.AssistantResponseDTO;
import com.example.assistant.service.impl.AssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assistant")  // ← Verify this path
@CrossOrigin(origins = "http://localhost:4200") // Optional: per-controller CORS
public class AssistantController {

    private final AssistantService assistantService;

    /**
     * Send a message to the assistant
     * POST /api/assistant/message
     */
    @PostMapping("/message")
    public ResponseEntity<AssistantResponseDTO> sendMessage(
            @Valid @RequestBody AssistantDTO assistantDTO) {

        log.info("Received assistant message request: {}", assistantDTO.getText());

        try {
            AssistantResponseDTO response = assistantService.proceedWithAssistant(assistantDTO);
            log.info("Assistant response generated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing assistant message", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AssistantResponseDTO.error("Failed to process message: " + e.getMessage()));
        }
    }

    /**
     * Get available commands
     * GET /api/assistant/commands
     */
    @GetMapping("/commands")
    public ResponseEntity<Map<String, String>> getAvailableCommands() {
        log.info("Fetching available commands");
        return ResponseEntity.ok(
                Map.of(
                        "message", "Use /help command to see available commands",
                        "endpoint", "/api/assistant/message"
                )
        );
    }

    /**
     * Health check endpoint
     * GET /api/assistant/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "service", "Assistant Service"
                )
        );
    }

    /**
     * Test endpoint to verify controller is working
     * GET /api/assistant/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Assistant controller is working!");
    }
}