/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.testutil;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.model.ActivityType;

import java.time.LocalDateTime;

public class ActivityTestDataBuilder {

    private Long id = 1L;
    private Long userId = 1L;
    private String username = "testuser";
    private ActivityType type = ActivityType.LOGIN;
    private String description = "Test activity";
    private String ipAddress = "127.0.0.1";
    private String deviceType = "Desktop";
    private String location = "Test Location";
    private LocalDateTime timestamp = LocalDateTime.now();

    public ActivityTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ActivityTestDataBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ActivityTestDataBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ActivityTestDataBuilder withType(ActivityType type) {
        this.type = type;
        return this;
    }

    public ActivityTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ActivityTestDataBuilder withLoginType() {
        this.type = ActivityType.LOGIN;
        this.description = "User logged in";
        return this;
    }

    public ActivityTestDataBuilder withLogoutType() {
        this.type = ActivityType.LOGOUT;
        this.description = "User logged out";
        return this;
    }

    public ActivityDTO build() {
        return ActivityDTO.builder()
                .id(id)
                .userId(userId)
                .username(username)
                .type(type)
                .description(description)
                .ipAddress(ipAddress)
                .deviceType(deviceType)
                .location(location)
                .timestamp(timestamp)
                .build();
    }
}
