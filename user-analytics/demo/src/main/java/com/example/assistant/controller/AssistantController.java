package com.example.assistant.controller;



import com.example.assistant.dto.AssistantDTO;

import com.example.assistant.dto.AssistantResponseDTO;
import com.example.assistant.service.impl.AssistantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/message")
    public ResponseEntity<AssistantResponseDTO> sendMessage(
            @Valid @RequestBody AssistantDTO assistantDTO) {
        log.info("Received assistant message request");
        AssistantResponseDTO response = assistantService.proceedWithAssistant(assistantDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/commands")
    public ResponseEntity<?> getAvailableCommands() {
        // Could expose available commands/intents
        return ResponseEntity.ok(
                Map.of("message", "Use /help command to see available commands")
        );
    }
}


