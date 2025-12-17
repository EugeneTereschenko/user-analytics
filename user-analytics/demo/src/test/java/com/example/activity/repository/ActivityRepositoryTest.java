/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.activity.repository;

import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import com.example.demo.UserAnalyticsJavaApplication;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
class ActivityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ActivityRepository activityRepository;

    @TestConfiguration
    static class MockHttpServletRequestConfig {
        @Bean
        @Primary
        public HttpServletRequest httpServletRequest() {
            return Mockito.mock(HttpServletRequest.class);
        }
    }



    @Test
    void findByUserId_shouldReturnActivities() {

        Activity activity = new Activity();
        activity.setUserId(1L);
        activity.setUsername("testuser");
        activity.setType(ActivityType.LOGIN);
        activity.setDescription("Test");
        activity.setTimestamp(LocalDateTime.now());
        entityManager.persist(activity);
        entityManager.flush();

        List<Activity> found = activityRepository.findByUserId(1L);

        assertThat(found).isNotEmpty();
        assertThat(found)
                .anyMatch(a -> "testuser".equals(a.getUsername()));
    }

    @Test
    void findByType_shouldReturnActivitiesOfType() {
        // Given
        Activity activity1 = createActivity(ActivityType.LOGIN);
        Activity activity2 = createActivity(ActivityType.LOGOUT);
        entityManager.persist(activity1);
        entityManager.persist(activity2);
        entityManager.flush();


        List<Activity> found = activityRepository.findByType(ActivityType.LOGIN);


        assertThat(found).hasSize(1);
        assertThat(found.get(0).getType()).isEqualTo(ActivityType.LOGIN);
    }

    @Test
    void findByTimestampBetween_shouldReturnActivitiesInRange() {

        LocalDateTime now = LocalDateTime.now();
        Activity activity = new Activity();
        activity.setUserId(1L);
        activity.setUsername("testuser");
        activity.setType(ActivityType.LOGIN);
        activity.setDescription("Test");
        activity.setTimestamp(now);
        entityManager.persist(activity);
        entityManager.flush();


        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);
        List<Activity> found = activityRepository.findByTimestampBetween(start, end);


        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getTimestamp()).isBetween(start, end);
    }

    @Test
    void countByType_shouldReturnCorrectCount() {

        entityManager.persist(createActivity(ActivityType.LOGIN));
        entityManager.persist(createActivity(ActivityType.LOGIN));
        entityManager.persist(createActivity(ActivityType.LOGOUT));
        entityManager.flush();

        long loginCount = activityRepository.countByType(ActivityType.LOGIN);
        long logoutCount = activityRepository.countByType(ActivityType.LOGOUT);

        assertThat(loginCount).isEqualTo(2L);
        assertThat(logoutCount).isEqualTo(1L);
    }

    private Activity createActivity(ActivityType type) {
        Activity activity = new Activity();
        activity.setUserId(1L);
        activity.setUsername("testuser");
        activity.setType(type);
        activity.setDescription("Test activity");
        activity.setTimestamp(LocalDateTime.now());
        return activity;
    }
}