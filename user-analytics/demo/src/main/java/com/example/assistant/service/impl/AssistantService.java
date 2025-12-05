package com.example.assistant.service.impl;

import com.example.assistant.dto.AssistantDTO;
import com.example.assistant.dto.AssistantResponseDTO;

import java.util.Map;

public interface AssistantService {
    AssistantResponseDTO proceedWithAssistant(AssistantDTO assistantDTO);
}
