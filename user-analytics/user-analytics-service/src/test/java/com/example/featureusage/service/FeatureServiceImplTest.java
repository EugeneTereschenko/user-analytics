/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.service;

import com.example.featureusage.repository.FeatureRatingRepository;
import com.example.featureusage.repository.FeatureUsageRepository;
import com.example.demo.repository.UserRepository;
import com.example.featureusage.dto.CategoryUsageDTO;
import com.example.featureusage.dto.FeatureUsageDTO;
import com.example.featureusage.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureServiceImpl Tests")
class FeatureServiceImplTest {

    @Mock
    FeatureUsageRepository featureUsageRepository;
    @Mock
    FeatureRatingRepository featureRatingRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    FeatureServiceImpl featureService;

    @Test
    void testGetFeatureUsage() {
        List<Object[]> stats = List.of(new Object[]{"Dashboard", 5L}, new Object[]{"Reports", 2L});
        when(featureUsageRepository.getFeatureUsageStats(any())).thenReturn(stats);

        FeatureUsageDTO dto = featureService.getFeatureUsage("weekly");

        assertEquals(List.of("Dashboard", "Reports"), dto.getFeatures());
        assertEquals(List.of(5, 2), dto.getUsageCounts());
    }

    @Test
    void testGetStatistics() {
        when(userRepository.count()).thenReturn(10L);
        when(featureUsageRepository.countActiveUsers(any())).thenReturn(5L);
        when(featureRatingRepository.getAverageRating()).thenReturn(4.5);
        when(featureUsageRepository.countTotalSessions()).thenReturn(20L);
        when(featureUsageRepository.getAverageSessionDuration()).thenReturn(120.0);

        UsageStatistics stats = featureService.getStatistics();

        assertEquals(10, stats.getTotalUsers());
        assertEquals(5, stats.getActiveUsers());
        assertEquals(50.0, stats.getEngagementRate());
        assertEquals(4.5, stats.getAvgRating());
        assertEquals(20, stats.getTotalSessions());
        assertEquals("2m 0s", stats.getAvgSessionDuration());
    }

    @Test
    void testGetFeatureDetails() {
        when(featureUsageRepository.getFeatureUsageStats(any()))
                .thenReturn(Collections.singletonList(new Object[]{"Dashboard", 10L}));
        when(featureUsageRepository.getFeatureUsageStats(any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new Object[]{"Dashboard", 5L}));
        when(featureUsageRepository.getUniqueUsersPerFeature(any()))
                .thenReturn(Collections.singletonList(new Object[]{"Dashboard", 3L}));
        when(featureUsageRepository.getAvgTimePerFeature())
                .thenReturn(Collections.singletonList(new Object[]{"Dashboard", 60.0}));
        when(featureRatingRepository.getAverageRatingPerFeature())
                .thenReturn(Collections.singletonList(new Object[]{"Dashboard", 4.0}));


        List<FeatureDetail> details = featureService.getFeatureDetails();

        assertFalse(details.isEmpty());
        FeatureDetail detail = details.get(0);
        assertEquals("Dashboard", detail.getName());
        assertEquals("Analytics", detail.getCategory());
        assertEquals(3, detail.getUsers());
        assertEquals("1m 0s", detail.getAvgTime());
        assertEquals(4.0, detail.getRating());
        assertEquals(5, detail.getSessions());
    }

    @Test
    void testGetUsageTrends() {
        FeatureUsage usage1 = new FeatureUsage();
        usage1.setTimestamp(LocalDateTime.now().minusDays(1));
        FeatureUsage usage2 = new FeatureUsage();
        usage2.setTimestamp(LocalDateTime.now());

        when(featureUsageRepository.findByTimestampBetween(any(), any()))
                .thenReturn(List.of(usage1, usage2));

        UsageTrend trend = featureService.getUsageTrends("weekly", null);

        assertEquals("weekly", trend.getPeriod());
        assertEquals(2, trend.getData().size());
    }

    @Test
    void testGetTopFeatures() {
        FeatureDetail detail1 = FeatureDetail.builder().name("A").users(5).build();
        FeatureDetail detail2 = FeatureDetail.builder().name("B").users(3).build();
        FeatureServiceImpl spyService = spy(featureService);
        doReturn(List.of(detail1, detail2)).when(spyService).getFeatureDetails();

        List<FeatureDetail> top = spyService.getTopFeatures(1);

        assertEquals(1, top.size());
        assertEquals("A", top.get(0).getName());
    }

    @Test
    void testGetUsageByCategory() {
        when(featureUsageRepository.getUsageByCategory())
                .thenReturn(List.of(new Object[]{"Analytics", 7L}, new Object[]{null, 2L}));

        CategoryUsageDTO dto = featureService.getUsageByCategory();

        assertEquals(List.of("Analytics", "Uncategorized"), dto.getCategories());
        assertEquals(List.of(7, 2), dto.getValues());
    }

    @Test
    void testTrackFeatureUsage() {
        when(featureUsageRepository.save(any(FeatureUsage.class))).thenReturn(null);
        featureService.trackFeatureUsage("Dashboard", 1L, 60);
        verify(featureUsageRepository, times(1)).save(any(FeatureUsage.class));
    }
}
