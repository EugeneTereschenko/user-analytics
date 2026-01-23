/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.controller;

import com.example.notification.dto.NotificationRequest;
import com.example.notification.dto.NotificationResponse;
import com.example.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Create and send notification
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        log.info("Creating notification for recipient: {}", request.getRecipientId());
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get notifications for a recipient
     */
    @GetMapping("/recipient/{recipientId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByRecipient(
            @PathVariable Long recipientId) {
        log.info("Fetching notifications for recipient: {}", recipientId);
        List<NotificationResponse> notifications =
                notificationService.getNotificationsByRecipient(recipientId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get notification by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        log.info("Fetching notification: {}", id);
        NotificationResponse notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    /**
     * Cancel scheduled notification
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> cancelNotification(@PathVariable Long id) {
        log.info("Cancelling notification: {}", id);
        notificationService.cancelNotification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Manually trigger scheduled notifications processing
     */
    @PostMapping("/process-scheduled")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<String> processScheduledNotifications() {
        log.info("Manual trigger: processing scheduled notifications");
        notificationService.processScheduledNotifications();
        return ResponseEntity.ok("Scheduled notifications processed");
    }

    /**
     * Manually retry failed notifications
     */
    @PostMapping("/retry-failed")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<String> retryFailedNotifications() {
        log.info("Manual trigger: retrying failed notifications");
        notificationService.retryFailedNotifications(3);
        return ResponseEntity.ok("Failed notifications retried");
    }
}
