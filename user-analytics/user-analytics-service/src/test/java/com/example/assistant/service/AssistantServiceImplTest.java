/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.assistant.service;

import com.example.assistant.dto.AssistantDTO;
import com.example.assistant.dto.AssistantResponseDTO;
import com.example.assistant.service.impl.Command;
import com.example.assistant.service.impl.Intent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Assistant Service Tests")
class AssistantServiceImplTest {

    @Mock
    CommandRegistry commandRegistry;

    @Mock
    IntentRegistry intentRegistry;

    @Mock
    Command command;

    @Mock
    Intent intent;

    @InjectMocks
    AssistantServiceImpl assistantService;

    @Test
    @DisplayName("Should return error for empty message")
    void testProceedWithAssistant_emptyMessage() {
        AssistantDTO dto = new AssistantDTO();
        dto.setText("Hello executed");
        AssistantResponseDTO response = assistantService.proceedWithAssistant(dto);
        assertTrue(response.getType().equalsIgnoreCase("success"));
        assertEquals("I'm not sure how to respond to that yet. Try asking about users, stats, or activity! Type /help for available commands.", response.getText());

    }

    @Test
    @DisplayName("Should handle command")
    void testProceedWithAssistant_command() {
        AssistantDTO dto = new AssistantDTO();
        dto.setText("/hello arg1 arg2");
        when(commandRegistry.getCommand("/hello")).thenReturn(Optional.of(command));
        when(command.execute(new String[]{"arg1", "arg2"})).thenReturn("Hello executed");

        AssistantResponseDTO response = assistantService.proceedWithAssistant(dto);

        assertTrue(response.getType().equalsIgnoreCase("success"));
        assertEquals("Hello executed", response.getText());
    }

    @Test
    @DisplayName("Should handle unknown command")
    void testProceedWithAssistant_unknownCommand() {
        AssistantDTO dto = new AssistantDTO();
        dto.setText("/unknown");
        when(commandRegistry.getCommand("/unknown")).thenReturn(Optional.empty());

        AssistantResponseDTO response = assistantService.proceedWithAssistant(dto);

        assertTrue(response.getType().equalsIgnoreCase("success"));
        assertEquals("Unknown command: /unknown. Type /help to see available commands.", response.getText());
    }

    @Test
    @DisplayName("Should handle intent")
    void testProceedWithAssistant_intent() {
        AssistantDTO dto = new AssistantDTO();
        dto.setText("show me stats");
        when(intentRegistry.matchIntent("show me stats")).thenReturn(Optional.of(intent));
        when(intent.handle("show me stats")).thenReturn("Stats shown");

        AssistantResponseDTO response = assistantService.proceedWithAssistant(dto);

        assertTrue(response.getType().equalsIgnoreCase("success"));
        assertEquals("Stats shown", response.getText());
    }

    @Test
    @DisplayName("Should return default response for unmatched intent")
    void testProceedWithAssistant_noIntentMatch() {
        AssistantDTO dto = new AssistantDTO();
        dto.setText("random text");
        when(intentRegistry.matchIntent("random text")).thenReturn(Optional.empty());

        AssistantResponseDTO response = assistantService.proceedWithAssistant(dto);

        assertTrue(response.getType().equalsIgnoreCase("success"));
        assertEquals("I'm not sure how to respond to that yet. Try asking about users, stats, or activity! Type /help for available commands.", response.getText());
    }

}