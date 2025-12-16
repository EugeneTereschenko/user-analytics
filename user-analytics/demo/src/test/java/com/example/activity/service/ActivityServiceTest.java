/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.activity.service;

import com.example.activity.dto.ActivityStatsDTO;
import com.example.activity.dto.ActivityDTO;
import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import com.example.activity.repository.ActivityRepository;
import com.example.demo.testutil.ActivityTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        activity = new Activity();
        activity.setId(1L);
        activity.setUserId(1L);
        activity.setUsername("testuser");
        activity.setType(ActivityType.LOGIN);
        activity.setDescription("Test activity");
        activity.setTimestamp(LocalDateTime.now());

        activityDTO = new ActivityTestDataBuilder()
                .withId(1L)
                .withUserId(1L)
                .withLoginType()
                .build();
    }

    @Test
    void getAllActivities_shouldReturnAllActivities() {

        when(activityRepository.findAllOrderByTimestampDesc())
                .thenReturn(Arrays.asList(activity));


        List<ActivityDTO> result = activityService.getAllActivities();


        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        verify(activityRepository, times(1)).findAllOrderByTimestampDesc();
    }

    @Test
    void getActivitiesByUserId_shouldReturnUserActivities() {

        Long userId = 1L;
        when(activityRepository.findByUserIdOrderByTimestampDesc(userId))
                .thenReturn(Arrays.asList(activity));

        List<ActivityDTO> result = activityService.getActivitiesByUserId(userId);


        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        verify(activityRepository).findByUserIdOrderByTimestampDesc(userId);
    }

    @Test
    void logActivity_shouldSaveActivity() {

        when(activityRepository.save(any(Activity.class))).thenReturn(activity);


        ActivityDTO result = activityService.logActivity(activityDTO);


        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void deleteActivity_shouldCallRepository() {

        Long activityId = 1L;
        doNothing().when(activityRepository).deleteById(activityId);


        activityService.deleteActivity(activityId);


        verify(activityRepository).deleteById(activityId);
    }

    @Test
    void getActivityStats_shouldReturnCorrectStats() {

        when(activityRepository.count()).thenReturn(100L);
        when(activityRepository.countByType(ActivityType.LOGIN)).thenReturn(25L);
        when(activityRepository.countByType(ActivityType.LOGOUT)).thenReturn(20L);
        when(activityRepository.countByType(ActivityType.CREATE)).thenReturn(15L);
        when(activityRepository.countByType(ActivityType.UPDATE)).thenReturn(30L);
        when(activityRepository.countByType(ActivityType.DELETE)).thenReturn(5L);
        when(activityRepository.countByType(ActivityType.VIEW)).thenReturn(5L);


        ActivityStatsDTO result = activityService.getActivityStats();

        assertThat(result.getTotalActivities()).isEqualTo(100L);
        assertThat(result.getLoginCount()).isEqualTo(25L);
        assertThat(result.getLogoutCount()).isEqualTo(20L);
    }
}
