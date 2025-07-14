package com.example.demo.controller;

import com.example.demo.dto.SupportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/support")
public class SupportController {


    @PostMapping
    public void submitFeedback(@RequestBody SupportDTO supportDTO) {
        log.info("Feedback received. Subject: {}, Message: {}", supportDTO.getSubject(), supportDTO.getMessage());
        // Handle feedback (e.g., save to DB, send email, etc.)
    }


}
