/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.controller;

import com.example.featureusage.dto.CategoryUsageDTO;
import com.example.featureusage.dto.FeatureUsageDTO;
import com.example.featureusage.dto.TrackUsageRequest;
import com.example.featureusage.model.FeatureDetail;
import com.example.featureusage.model.UsageStatistics;
import com.example.featureusage.model.UsageTrend;
import com.example.featureusage.service.impl.FeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
@CrossOrigin(origins = "http://localhost:4200")
public class FeatureUsageController {

    private final FeatureService featureService;

    @GetMapping("/feature")
    public ResponseEntity<FeatureUsageDTO> getFeatureUsage(
            @RequestParam(defaultValue = "weekly") String period) {
        log.info("GET /api/usage/feature?period={}", period);
        FeatureUsageDTO data = featureService.getFeatureUsage(period);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/statistics")
    public ResponseEntity<UsageStatistics> getStatistics() {
        log.info("GET /api/usage/statistics");
        UsageStatistics stats = featureService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/feature/details")
    public ResponseEntity<List<FeatureDetail>> getFeatureDetails() {
        log.info("GET /api/usage/feature/details");
        List<FeatureDetail> details = featureService.getFeatureDetails();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/trends")
    public ResponseEntity<UsageTrend> getUsageTrends(
            @RequestParam(defaultValue = "weekly") String period,
            @RequestParam(required = false) String feature) {
        log.info("GET /api/usage/trends?period={}&feature={}", period, feature);
        UsageTrend trend = featureService.getUsageTrends(period, feature);
        return ResponseEntity.ok(trend);
    }

    @GetMapping("/feature/top")
    public ResponseEntity<List<FeatureDetail>> getTopFeatures(
            @RequestParam(defaultValue = "5") int limit) {
        log.info("GET /api/usage/feature/top?limit={}", limit);
        List<FeatureDetail> topFeatures = featureService.getTopFeatures(limit);
        return ResponseEntity.ok(topFeatures);
    }

    @GetMapping("/category")
    public ResponseEntity<CategoryUsageDTO> getUsageByCategory() {
        log.info("GET /api/usage/category");
        CategoryUsageDTO data = featureService.getUsageByCategory();
        return ResponseEntity.ok(data);
    }

    @PostMapping("/track")
    public ResponseEntity<Void> trackFeatureUsage(@RequestBody TrackUsageRequest request) {
        log.info("POST /api/usage/track - Feature: {}", request.getFeatureName());
        featureService.trackFeatureUsage(
                request.getFeatureName(),
                request.getUserId(),
                request.getDuration()
        );
        return ResponseEntity.ok().build();
    }
}
