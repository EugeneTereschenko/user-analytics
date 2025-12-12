/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.service.impl;

import com.example.demo.dto.ResponseDTO;
import com.example.notification.dto.NotificationsDTO;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationService {
    List<NotificationsDTO> getAllNotifications();

    List<NotificationsDTO> getUnreadNotifications();

    List<NotificationsDTO> getNotificationsByType(NotificationType type);

    List<NotificationsDTO> getNotificationsByPriority(NotificationPriority priority);

    List<NotificationsDTO> searchNotifications(String query);

    @Transactional
    ResponseDTO createNotification(NotificationsDTO notificationsDTO);

    @Transactional
    void markAsRead(Long notificationId);

    @Transactional
    void markAllAsRead();

    @Transactional
    void deleteNotification(Long notificationId);

    @Transactional
    void deleteAllReadNotifications();

    Long getUnreadCount();
}
