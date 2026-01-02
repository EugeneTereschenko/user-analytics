package com.example.assistant.service;


import com.example.assistant.dto.AssistantDTO;
import com.example.assistant.dto.AssistantResponseDTO;
import com.example.assistant.service.impl.AssistantService;
import com.example.assistant.service.impl.Command;
import com.example.assistant.service.impl.Intent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssistantServiceImpl implements AssistantService {

    private final CommandRegistry commandRegistry;
    private final IntentRegistry intentRegistry;

    @Override
    public AssistantResponseDTO proceedWithAssistant(AssistantDTO assistantDTO) {
        String incomingText = assistantDTO.getText();

        // Validation
        if (incomingText == null || incomingText.isBlank()) {
            log.warn("Received null or empty message from assistant");
            return AssistantResponseDTO.error("Message text cannot be null or empty");
        }

        log.info("Processing message: {}", incomingText);

        String normalizedInput = incomingText.toLowerCase().trim();
        String reply;

        try {
            // Check for commands first
            if (normalizedInput.startsWith("/")) {
                reply = handleCommand(normalizedInput);
            } else {
                // Try to match intents
                reply = handleIntent(normalizedInput);
            }

            return AssistantResponseDTO.success(reply);

        } catch (Exception e) {
            log.error("Error processing assistant message: {}", e.getMessage(), e);
            return AssistantResponseDTO.error(
                    "Sorry, I encountered an error processing your request. Please try again."
            );
        }
    }

    private String handleCommand(String input) {
        // Parse command and arguments
        String[] parts = input.split("\\s+");
        String commandName = parts[0];
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        // Find and execute command
        Optional<Command> command = commandRegistry.getCommand(commandName);

        if (command.isPresent()) {
            log.debug("Executing command: {}", commandName);
            return command.get().execute(args);
        } else {
            log.warn("Unknown command: {}", commandName);
            return "Unknown command: " + commandName +
                    ". Type /help to see available commands.";
        }
    }

    private String handleIntent(String input) {
        // Try to match an intent
        Optional<Intent> matchedIntent = intentRegistry.matchIntent(input);

        if (matchedIntent.isPresent()) {
            log.debug("Matched intent: {}", matchedIntent.get().getClass().getSimpleName());
            return matchedIntent.get().handle(input);
        } else {
            log.debug("No intent matched for input: {}", input);
            return getDefaultResponse();
        }
    }

    private String getDefaultResponse() {
        return "I'm not sure how to respond to that yet. " +
                "Try asking about users, stats, or activity! " +
                "Type /help for available commands.";
    }
}
