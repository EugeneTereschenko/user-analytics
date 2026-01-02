/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.notification.dto.NotificationsDTO;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import com.example.notification.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationsDTO>> getAllNotifications() {
        log.info("GET /api/notifications - Fetching all notifications");
        List<NotificationsDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationsDTO>> getUnreadNotifications() {
        log.info("GET /api/notifications/unread");
        List<NotificationsDTO> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationsDTO>> getNotificationsByType(
            @PathVariable NotificationType type) {
        log.info("GET /api/notifications/type/{}", type);
        List<NotificationsDTO> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<NotificationsDTO>> getNotificationsByPriority(
            @PathVariable NotificationPriority priority) {
        log.info("GET /api/notifications/priority/{}", priority);
        List<NotificationsDTO> notifications = notificationService.getNotificationsByPriority(priority);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NotificationsDTO>> searchNotifications(@RequestParam String q) {
        log.info("GET /api/notifications/search?q={}", q);
        List<NotificationsDTO> notifications = notificationService.searchNotifications(q);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount() {
        log.info("GET /api/notifications/unread/count");
        Long count = notificationService.getUnreadCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createNotification(
            @RequestBody NotificationsDTO notificationsDTO) {
        log.info("POST /api/notifications");
        ResponseDTO response = notificationService.createNotification(notificationsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("POST /api/notifications/{}/read", id);
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        log.info("POST /api/notifications/read-all");
        notificationService.markAllAsRead();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("DELETE /api/notifications/{}", id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/read")
    public ResponseEntity<Void> deleteAllReadNotifications() {
        log.info("DELETE /api/notifications/read");
        notificationService.deleteAllReadNotifications();
        return ResponseEntity.noContent().build();
    }
}
