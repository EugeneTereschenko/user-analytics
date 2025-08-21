package com.example.demo.controller;


import com.example.demo.dto.AssistantDTO;
import com.example.demo.service.impl.AssistantService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody AssistantDTO assistantDTO) {
        return ResponseEntity.ok(assistantService.proceedWithAssistant(assistantDTO));
    }
}


