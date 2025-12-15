/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.activity.service;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.dto.ActivityStatsDTO;
import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import com.example.demo.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private Activity activity;
    private ActivityDTO activityDTO;

    @BeforeEach
    void setUp() {
        activity = new Activity.Builder()
                .userId(2L)
                .username("testuser")
                .type(ActivityType.LOGIN)
                .description("desc")
                .ipAddress("127.0.0.1")
                .deviceType("Desktop")
                .location("Earth")
                .build();

        activityDTO = new ActivityDTO.Builder()
                .id(1L)
                .userId(2L)
                .username("testuser")
                .type(ActivityType.LOGIN)
                .description("desc")
                .ipAddress("127.0.0.1")
                .deviceType("Desktop")
                .location("Earth")
                .timestamp(activity.getTimestamp())
                .build();
    }

    @Test
    void testLogActivity() {
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        ActivityDTO result = activityService.logActivity(activityDTO);

        assertNotNull(result);
        assertEquals(activityDTO.getUserId(), result.getUserId());
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void testGetAllActivities() {
        when(activityRepository.findAllOrderByTimestampDesc()).thenReturn(Collections.singletonList(activity));

        List<ActivityDTO> result = activityService.getAllActivities();

        assertEquals(1, result.size());
        assertEquals(activity.getUserId(), result.get(0).getUserId());
    }

    @Test
    void testGetActivitiesByUserId() {
        when(activityRepository.findByUserIdOrderByTimestampDesc(2L)).thenReturn(Collections.singletonList(activity));

        List<ActivityDTO> result = activityService.getActivitiesByUserId(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getUserId());
    }

    @Test
    void testGetActivitiesByType() {
        when(activityRepository.findByType(ActivityType.LOGIN)).thenReturn(Collections.singletonList(activity));

        List<ActivityDTO> result = activityService.getActivitiesByType("login");

        assertEquals(1, result.size());
        assertEquals(ActivityType.LOGIN, result.get(0).getType());
    }

    @Test
    void testGetActivitiesByDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(activityRepository.findByTimestampBetween(start, end)).thenReturn(Collections.singletonList(activity));

        List<ActivityDTO> result = activityService.getActivitiesByDateRange(start, end);

        assertEquals(1, result.size());
    }

    @Test
    void testGetRecentActivities() {
        when(activityRepository.findTop10ByOrderByTimestampDesc()).thenReturn(Arrays.asList(activity, activity));

        List<ActivityDTO> result = activityService.getRecentActivities(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetActivityStats() {
        when(activityRepository.count()).thenReturn(10L);
        when(activityRepository.countByType(ActivityType.LOGIN)).thenReturn(2L);
        when(activityRepository.countByType(ActivityType.LOGOUT)).thenReturn(1L);
        when(activityRepository.countByType(ActivityType.CREATE)).thenReturn(3L);
        when(activityRepository.countByType(ActivityType.UPDATE)).thenReturn(1L);
        when(activityRepository.countByType(ActivityType.DELETE)).thenReturn(2L);
        when(activityRepository.countByType(ActivityType.VIEW)).thenReturn(1L);

        ActivityStatsDTO stats = activityService.getActivityStats();

        assertEquals(10L, stats.getTotalActivities());
        assertEquals(2L, stats.getLoginCount());
    }

    @Test
    void testDeleteActivity() {
        activityService.deleteActivity(1L);
        verify(activityRepository).deleteById(1L);
    }

    @Test
    void testClearAllActivities() {
        activityService.clearAllActivities();
        verify(activityRepository).deleteAll();
    }
}
