package com.example.demo.service.impl;

import com.example.demo.dto.NotificationsDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Notification;

import java.util.List;

public interface NotificationService {
    ResponseDTO createNotification(NotificationsDTO notificationsDTO);
    List<NotificationsDTO> getAllNotifications();
    Notification saveNotification(NotificationsDTO notificationsDTO);
}
