package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.SupportDTO;
import com.example.demo.service.impl.SupportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/support")
public class SupportController {

    private final SupportService supportService;

    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody SupportDTO supportDTO) {
        log.info("Feedback received. Subject: {}, Message: {}", supportDTO.getSubject(), supportDTO.getMessage());
        ResponseDTO responseDTO = supportService.createSupport(supportDTO);
        return ResponseEntity.ok(responseDTO);
    }


}
