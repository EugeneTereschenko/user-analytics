package com.example.demo.controller;

import com.example.demo.dto.StatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/system/status")
public class StatusController {


    @GetMapping
    public ResponseEntity<?> getStatus() {
        log.info("Fetching system status");
        // Mock data for demonstration purposes
        String status = "System is running smoothly";
        return ResponseEntity.ok(new StatusDTO.Builder()
                        .cpu("2.5 GHz")
                        .memory("8 GB")
                        .apiLatency("100 ms")
                        .uptime("24 days, 5 hours")
                        .jobs("5 active jobs")
                .build());
    }
}
