package com.example.demo.service.impl;

import com.example.demo.dto.AssistantDTO;

import java.util.Map;

public interface AssistantService {
    Map<String, String> proceedWithAssistant(AssistantDTO assistantDTO);
}
