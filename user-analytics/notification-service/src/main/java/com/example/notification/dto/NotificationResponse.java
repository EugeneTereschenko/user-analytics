/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.dto;

import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long recipientId;
    private String recipientEmail;
    private String recipientPhone;
    private String recipientName;
    private NotificationType notificationType;
    private NotificationChannel channel;
    private String subject;
    private String message;
    private NotificationStatus status;
    private LocalDateTime scheduledTime;
    private LocalDateTime sentTime;
    private String errorMessage;
    private LocalDateTime createdAt;
}
