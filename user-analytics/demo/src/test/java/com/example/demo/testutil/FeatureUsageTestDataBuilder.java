/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.testutil;

import com.example.featureusage.model.FeatureUsage;

import java.time.LocalDateTime;

public class FeatureUsageTestDataBuilder {
    private Long id = null;
    private Long userId = 1L;
    private String featureName = "Dashboard";
    private String category = "Analytics";
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer durationSeconds = 60;
    private String sessionId = "session-1";
    private String deviceType = "Web";
    private String ipAddress = "127.0.0.1";

    public FeatureUsageTestDataBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public FeatureUsageTestDataBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public FeatureUsageTestDataBuilder featureName(String featureName) {
        this.featureName = featureName;
        return this;
    }

    public FeatureUsageTestDataBuilder category(String category) {
        this.category = category;
        return this;
    }

    public FeatureUsageTestDataBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public FeatureUsageTestDataBuilder durationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    public FeatureUsageTestDataBuilder sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public FeatureUsageTestDataBuilder deviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public FeatureUsageTestDataBuilder ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public FeatureUsage build() {
        return new FeatureUsage(
                id,
                userId,
                featureName,
                category,
                timestamp,
                durationSeconds,
                sessionId,
                deviceType,
                ipAddress
        );
    }
}

