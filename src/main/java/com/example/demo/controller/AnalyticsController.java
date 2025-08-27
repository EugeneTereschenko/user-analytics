package com.example.demo.controller;
import com.example.demo.dto.UserSummaryDTO;
import com.example.demo.service.impl.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/analytics/users")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<UserSummaryDTO> getUserSummary(
            @RequestParam(name = "startDate") LocalDate start,
            @RequestParam(name = "endDate") LocalDate end) {
        log.info("Fetching user summary from {} to {}", start, end);
        return ResponseEntity.ok(analyticsService.getUserSummary(start, end));
    }

    @GetMapping("/signups")
    public ResponseEntity<int[]> getSignups(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        log.info("Fetching signups from {} to {}", start, end);
        return ResponseEntity.ok(analyticsService.getSignups(start, end));
    }

    @GetMapping("/devices")
    public ResponseEntity<Map<String, Integer>> getDeviceBreakdown() {
        log.info("Fetching device breakdown");
        return ResponseEntity.ok(analyticsService.getDeviceBreakdown());
    }

    @GetMapping("/locations")
    public ResponseEntity<Map<String, Integer>> getUserLocations() {
        log.info("Fetching user locations");
        return ResponseEntity.ok(analyticsService.getUserLocations());
    }
}