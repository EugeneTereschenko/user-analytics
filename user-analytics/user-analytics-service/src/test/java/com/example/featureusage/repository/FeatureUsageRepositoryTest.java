/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.repository;

import com.example.demo.dto.UserEventDTO;
import com.example.demo.testutil.FeatureUsageTestDataBuilder;
import com.example.featureusage.model.FeatureUsage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import com.example.demo.UserAnalyticsJavaApplication;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
class FeatureUsageRepositoryTest {

    @Autowired
    private FeatureUsageRepository featureUsageRepository;

    @MockBean
    private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

    @Test
    @DisplayName("findByUserId returns correct usages")
    void testFindByUserId() {
        FeatureUsage usage1 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Reports")
                .durationSeconds(45)
                .build();

        FeatureUsage usage2 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Reports")
                .durationSeconds(45)
                .build();

        FeatureUsage usage3 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Dashboard")
                .durationSeconds(60)
                .build();
        featureUsageRepository.save(usage1);
        featureUsageRepository.save(usage2);
        featureUsageRepository.save(usage3);

        List<FeatureUsage> result = featureUsageRepository.findByUserId(1L);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> u.getUserId().equals(1L)));
    }

    @Test
    @DisplayName("findByFeatureName returns correct usages")
    void testFindByFeatureName() {
        FeatureUsage usage1 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Reports")
                .durationSeconds(45)
                .build();

        FeatureUsage usage2 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Dashboard")
                .durationSeconds(45)
                .build();

        FeatureUsage usage3 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Dashboard")
                .durationSeconds(60)
                .build();
        featureUsageRepository.save(usage1);
        featureUsageRepository.save(usage2);
        featureUsageRepository.save(usage3);

        List<FeatureUsage> result = featureUsageRepository.findByFeatureName("Dashboard");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> u.getFeatureName().equals("Dashboard")));
    }

    @Test
    @DisplayName("getFeatureUsageStats returns correct stats")
    void testGetFeatureUsageStats() {
        LocalDateTime now = LocalDateTime.now();
        FeatureUsage usage1 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Dashboard")
                .durationSeconds(60)
                .timestamp(now.minusDays(1))
                .build();

        FeatureUsage usage2 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Dashboard")
                .durationSeconds(30)
                .timestamp(now)
                .build();

        FeatureUsage usage3 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Reports")
                .durationSeconds(45)
                .timestamp(now)
                .build();

        featureUsageRepository.save(usage1);
        featureUsageRepository.save(usage2);
        featureUsageRepository.save(usage3);

        List<Object[]> stats = featureUsageRepository.getFeatureUsageStats(now.minusDays(2));
        assertFalse(stats.isEmpty());
        boolean foundDashboard = stats.stream().anyMatch(row -> "Dashboard".equals(row[0]));
        assertTrue(foundDashboard);
    }

    @Test
    @DisplayName("getAvgTimePerFeature returns correct averages")
    void testGetAvgTimePerFeature() {
        LocalDateTime now = LocalDateTime.now();
        FeatureUsage usage1 = new FeatureUsageTestDataBuilder()
                .userId(1L)
                .featureName("Dashboard")
                .durationSeconds(60)
                .timestamp(now)
                .build();

        FeatureUsage usage2 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Dashboard")
                .durationSeconds(30)
                .timestamp(now)
                .build();

        FeatureUsage usage3 = new FeatureUsageTestDataBuilder()
                .userId(2L)
                .featureName("Reports")
                .durationSeconds(45)
                .timestamp(now)
                .build();
        featureUsageRepository.save(usage1);
        featureUsageRepository.save(usage2);
        featureUsageRepository.save(usage3);

        List<Object[]> result = featureUsageRepository.getAvgTimePerFeature();
        assertEquals(2, result.size());
        for (Object[] row : result) {
            String featureName = (String) row[0];
            Double avg = (Double) row[1];
            if ("Dashboard".equals(featureName)) {
                assertEquals(45.0, avg, 0.01);
            } else if ("Reports".equals(featureName)) {
                assertEquals(45.0, avg, 0.01);
            } else {
                fail("Unexpected feature: " + featureName);
            }
        }
    }
}
