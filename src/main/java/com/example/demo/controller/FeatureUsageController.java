package com.example.demo.controller;

import com.example.demo.dto.FeatureUsageDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/usage/feature")
public class FeatureUsageController {

    @GetMapping
    public ResponseEntity<?> getFeatureUsage() {
        return ResponseEntity.ok(new FeatureUsageDTO.Builder()
                        .features(List.of("Feature A", "Feature B", "Feature C"))
                        .usageCounts(List.of(100, 200, 150))
                        .build());
    }
}

