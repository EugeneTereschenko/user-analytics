package com.example.activity.controller;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.dto.ActivityStatsDTO;
import com.example.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Adjust to your Angular URL
public class ActivityController {

    private final ActivityService activityService;

    /**
     * Get all activities
     */
    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {
        log.info("GET /api/activities - Fetching all activities");
        List<ActivityDTO> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByUserId(@PathVariable Long userId) {
        log.info("GET /api/activities/user/{} - Fetching user activities", userId);
        List<ActivityDTO> activities = activityService.getActivitiesByUserId(userId);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByType(@PathVariable String type) {
        log.info("GET /api/activities/type/{} - Fetching activities by type", type);
        try {
            List<ActivityDTO> activities = activityService.getActivitiesByType(type);
            return ResponseEntity.ok(activities);
        } catch (IllegalArgumentException e) {
            log.error("Invalid activity type: {}", type);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get activities by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/activities/date-range - Fetching activities between {} and {}", startDate, endDate);
        List<ActivityDTO> activities = activityService.getActivitiesByDateRange(startDate, endDate);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get recent activities
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ActivityDTO>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/activities/recent?limit={}", limit);
        List<ActivityDTO> activities = activityService.getRecentActivities(limit);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activity statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<ActivityStatsDTO> getActivityStats() {
        log.info("GET /api/activities/stats - Fetching activity statistics");
        ActivityStatsDTO stats = activityService.getActivityStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Log a new activity
     */
    @PostMapping
    public ResponseEntity<ActivityDTO> logActivity(@RequestBody ActivityDTO activityDTO) {
        log.info("POST /api/activities - Logging new activity");
        ActivityDTO savedActivity = activityService.logActivity(activityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedActivity);
    }

    /**
     * Delete an activity
     */
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        log.info("DELETE /api/activities/{}", activityId);
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear all activities
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllActivities() {
        log.warn("DELETE /api/activities/clear - Clearing all activities");
        activityService.clearAllActivities();
        return ResponseEntity.noContent().build();
    }
}
