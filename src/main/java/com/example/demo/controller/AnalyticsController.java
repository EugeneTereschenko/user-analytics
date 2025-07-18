package com.example.demo.controller;
import com.example.demo.dto.UserSummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/analytics/users")
public class AnalyticsController {

    @GetMapping("/summary")
    public ResponseEntity<UserSummaryDTO> getUserSummary(
            @RequestParam(name = "startDate") LocalDate start,
            @RequestParam(name = "endDate") LocalDate end) {
        log.info("Fetching user summary from {} to {}", start, end);
        UserSummaryDTO summary = new UserSummaryDTO(12345, 345, 56, 42.7);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/signups")
    public ResponseEntity<int[]> getSignups(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        log.info("Fetching signups from {} to {}", start, end);
        int[] signups = {10, 20, 15, 30, 25}; // Mock data for 5 days
        return ResponseEntity.ok(signups);
    }

    @GetMapping("/devices")
    public ResponseEntity<Map<String, Integer>> getDeviceBreakdown() {
        log.info("Fetching device breakdown");
        Map<String, Integer> data = Map.of("Desktop", 5000, "Mobile", 7000, "Tablet", 1500);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/locations")
    public ResponseEntity<Map<String, Integer>> getUserLocations() {
        log.info("Fetching user locations");
        Map<String, Integer> data = Map.of("USA", 4000, "India", 3000, "Germany", 1000);
        return ResponseEntity.ok(data);
    }
}