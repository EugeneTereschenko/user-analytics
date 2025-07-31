package com.example.demo.controller;

import com.example.demo.dto.StatusDTO;
import com.example.demo.service.impl.StatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/system/status")
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<?> getStatus() {
        log.info("Fetching system status");

         return ResponseEntity.ok(statusService.getLastStatus());
    }
}
