package com.example.demo.controller;

import com.example.demo.service.impl.FeatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/usage/feature")
public class FeatureUsageController {

    private final FeatureService featureService;

    @GetMapping
    public ResponseEntity<?> getFeatureUsage() {
        return ResponseEntity.ok(featureService.getFeatureUsage());
    }
}

