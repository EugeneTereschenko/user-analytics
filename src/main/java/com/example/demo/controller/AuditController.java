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
        return ResponseEntity.ok(auditService.getAllAudits()); // Placeholder implementation
    }
}
