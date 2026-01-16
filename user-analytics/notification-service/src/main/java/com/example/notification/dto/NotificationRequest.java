/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.dto;

import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    @Email(message = "Valid email is required")
    private String recipientEmail;

    private String recipientPhone;

    private String recipientName;

    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;

    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    private String subject;

    @NotNull(message = "Message is required")
    private String message;

    private LocalDateTime scheduledTime;

    private Map<String, Object> metadata;
}
