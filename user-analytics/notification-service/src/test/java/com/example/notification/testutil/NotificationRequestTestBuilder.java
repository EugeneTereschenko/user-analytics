/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.testutil;

import com.example.notification.dto.NotificationRequest;
import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class NotificationRequestTestBuilder {
    private Long recipientId = 1L;
    private String recipientEmail = "test@example.com";
    private String recipientPhone = "1234567890";
    private String recipientName = "Test User";
    private NotificationType notificationType = NotificationType.BILLING_NOTIFICATION;
    private NotificationChannel channel = NotificationChannel.EMAIL;
    private String subject = "Test Subject";
    private String message = "Test Message";
    private LocalDateTime scheduledTime = LocalDateTime.now();
    private Map<String, Object> metadata = new HashMap<>();
    private Long userId = 2L;

    public NotificationRequestTestBuilder withRecipientId(Long recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public NotificationRequestTestBuilder withRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    public NotificationRequestTestBuilder withRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
        return this;
    }

    public NotificationRequestTestBuilder withRecipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public NotificationRequestTestBuilder withNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public NotificationRequestTestBuilder withChannel(NotificationChannel channel) {
        this.channel = channel;
        return this;
    }

    public NotificationRequestTestBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public NotificationRequestTestBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public NotificationRequestTestBuilder withScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public NotificationRequestTestBuilder withMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public NotificationRequestTestBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public NotificationRequest build() {
        return NotificationRequest.builder()
                .recipientId(recipientId)
                .recipientEmail(recipientEmail)
                .recipientPhone(recipientPhone)
                .recipientName(recipientName)
                .notificationType(notificationType)
                .channel(channel)
                .subject(subject)
                .message(message)
                .scheduledTime(scheduledTime)
                .metadata(metadata)
                .userId(userId)
                .build();
    }
}

