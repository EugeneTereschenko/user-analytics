/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.dto;

import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsDTO {
    private Long id;
    private String title;
    private String message;
    private String timestamp;
    private NotificationType type;
    private NotificationPriority priority;
    private Boolean isRead;
    private String readAt;
    private String category;
    private String actionUrl;
    private String sender;
    private String metadata;
}
