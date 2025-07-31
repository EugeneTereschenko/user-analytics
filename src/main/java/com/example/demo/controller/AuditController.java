package com.example.demo.controller;

import com.example.demo.dto.AuditDTO;
import com.example.demo.service.impl.AuditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/audit/logs")
public class AuditController {

    private final AuditService auditService;


    @GetMapping
    public ResponseEntity<List<AuditDTO>> getLogs() {
        List<AuditDTO> auditLogs = new ArrayList<>();
        auditLogs.add(new AuditDTO.Builder()
                .timestamp("2023-10-01T10:00:00Z")
                .user("12345")
                .action("Login")
                .target("User logged in successfully.")
                .build());
        auditLogs.add(new AuditDTO.Builder()
                .timestamp("2023-10-01T10:05:00Z")
                .user("12345")
                .action("Update Profile")
                .target("User updated their profile information.")
                .build());


        return ResponseEntity.ok(auditService.getAllAudits()); // Placeholder implementation
    }
}
