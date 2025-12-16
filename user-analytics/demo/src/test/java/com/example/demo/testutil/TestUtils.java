/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.testutil;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Activity createTestActivity(Long id, Long userId, ActivityType type) {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setUserId(userId);
        activity.setUsername("testuser");
        activity.setType(type);
        activity.setDescription("Test activity");
        activity.setTimestamp(LocalDateTime.now());
        return activity;
    }

    public static ActivityDTO createTestActivityDTO(Long id, Long userId, ActivityType type) {
        return ActivityDTO.builder()
                .id(id)
                .userId(userId)
                .username("testuser")
                .type(type)
                .description("Test activity")
                .timestamp(LocalDateTime.now())
                .build();
    }
}

