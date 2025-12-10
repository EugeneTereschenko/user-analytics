package com.example.activity.service;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.dto.ActivityStatsDTO;
import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import com.example.demo.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    /**
     * Log a new activity
     */
    @Transactional
    public ActivityDTO logActivity(ActivityDTO activityDTO) {
        log.info("Logging activity: {} for user: {}", activityDTO.getType(), activityDTO.getUsername());

        Activity activity = new Activity.Builder()
                .userId(activityDTO.getUserId())
                .username(activityDTO.getUsername())
                .type(activityDTO.getType())
                .description(activityDTO.getDescription())
                .ipAddress(activityDTO.getIpAddress())
                .deviceType(activityDTO.getDeviceType())
                .location(activityDTO.getLocation())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        return convertToDTO(savedActivity);
    }

    /**
     * Get all activities
     */
    public List<ActivityDTO> getAllActivities() {
        log.debug("Fetching all activities");
        return activityRepository.findAllOrderByTimestampDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activities by user ID
     */
    public List<ActivityDTO> getActivitiesByUserId(Long userId) {
        log.debug("Fetching activities for user ID: {}", userId);
        return activityRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activities by type
     */
    public List<ActivityDTO> getActivitiesByType(String type) {
        log.debug("Fetching activities by type: {}", type);
        ActivityType activityType = ActivityType.valueOf(type.toUpperCase());
        return activityRepository.findByType(activityType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activities within date range
     */
    public List<ActivityDTO> getActivitiesByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching activities between {} and {}", start, end);
        return activityRepository.findByTimestampBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get recent activities
     */
    public List<ActivityDTO> getRecentActivities(int limit) {
        log.debug("Fetching {} recent activities", limit);
        return activityRepository.findTop10ByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activity statistics
     */
    public ActivityStatsDTO getActivityStats() {
        log.debug("Calculating activity statistics");

        long total = activityRepository.count();
        long loginCount = activityRepository.countByType(ActivityType.LOGIN);
        long logoutCount = activityRepository.countByType(ActivityType.LOGOUT);
        long createCount = activityRepository.countByType(ActivityType.CREATE);
        long updateCount = activityRepository.countByType(ActivityType.UPDATE);
        long deleteCount = activityRepository.countByType(ActivityType.DELETE);
        long viewCount = activityRepository.countByType(ActivityType.VIEW);

        return new ActivityStatsDTO(total, loginCount, logoutCount,
                createCount, updateCount, deleteCount, viewCount);
    }

    /**
     * Delete an activity
     */
    @Transactional
    public void deleteActivity(Long activityId) {
        log.info("Deleting activity with ID: {}", activityId);
        activityRepository.deleteById(activityId);
    }

    /**
     * Clear all activities
     */
    @Transactional
    public void clearAllActivities() {
        log.warn("Clearing all activities");
        activityRepository.deleteAll();
    }

    /**
     * Convert Activity entity to DTO
     */
    private ActivityDTO convertToDTO(Activity activity) {
        return new ActivityDTO.Builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .username(activity.getUsername())
                .type(activity.getType())
                .description(activity.getDescription())
                .timestamp(activity.getTimestamp())
                .ipAddress(activity.getIpAddress())
                .deviceType(activity.getDeviceType())
                .location(activity.getLocation())
                .build();
    }
}
