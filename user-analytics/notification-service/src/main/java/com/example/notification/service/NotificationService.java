/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.service;

import com.example.notification.dto.NotificationRequest;
import com.example.notification.dto.NotificationResponse;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationStatus;
import com.example.notification.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final ObjectMapper objectMapper;

    /**
     * Create and send notification
     */
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        log.info("Creating notification for recipient: {}", request.getRecipientId());

        request.checkUserId();

        Notification notification = Notification.builder()
                .recipientId(request.getRecipientId())
                .recipientEmail(request.getRecipientEmail())
                .recipientPhone(request.getRecipientPhone())
                .recipientName(request.getRecipientName())
                .notificationType(request.getNotificationType())
                .channel(request.getChannel())
                .subject(request.getSubject())
                .message(request.getMessage())
                .scheduledTime(request.getScheduledTime())
                .userId(request.getUserId())
                .status(request.getScheduledTime() != null ?
                        NotificationStatus.SCHEDULED : NotificationStatus.PENDING)
                .retryCount(0)
                .build();

        if (request.getMetadata() != null) {
            try {
                notification.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            } catch (Exception e) {
                log.error("Failed to serialize metadata", e);
            }
        }

        Notification saved = notificationRepository.save(notification);

        // Send immediately if not scheduled
        if (request.getScheduledTime() == null) {
            sendNotification(saved);
        }

        return mapToResponse(saved);
    }

    /**
     * Send notification via appropriate channel
     */
    @Transactional
    public void sendNotification(Notification notification) {
        log.info("Sending notification ID: {} via {}", notification.getId(), notification.getChannel());

        try {
            switch (notification.getChannel()) {
                case EMAIL -> sendEmailNotification(notification);
                case SMS -> sendSmsNotification(notification);
                case BOTH -> {
                    sendEmailNotification(notification);
                    sendSmsNotification(notification);
                }
            }

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentTime(LocalDateTime.now());
            log.info("Notification sent successfully: {}", notification.getId());

        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
        }

        notificationRepository.save(notification);
    }

    private void sendEmailNotification(Notification notification) {
        if (notification.getRecipientEmail() == null) {
            throw new IllegalArgumentException("Recipient email is required");
        }

        switch (notification.getNotificationType()) {
            case APPOINTMENT_REMINDER, APPOINTMENT_CONFIRMATION ->
                    emailService.sendSimpleEmail(
                            notification.getRecipientEmail(),
                            notification.getSubject(),
                            notification.getMessage()
                    );
            case TEST_RESULT_ALERT ->
                    emailService.sendSimpleEmail(
                            notification.getRecipientEmail(),
                            "Test Results Available",
                            notification.getMessage()
                    );
            case PRESCRIPTION_REMINDER ->
                    emailService.sendSimpleEmail(
                            notification.getRecipientEmail(),
                            "Prescription Reminder",
                            notification.getMessage()
                    );
            default ->
                    emailService.sendSimpleEmail(
                            notification.getRecipientEmail(),
                            notification.getSubject(),
                            notification.getMessage()
                    );
        }
    }

    private void sendSmsNotification(Notification notification) {
        if (notification.getRecipientPhone() == null) {
            throw new IllegalArgumentException("Recipient phone is required");
        }

        smsService.sendSms(notification.getRecipientPhone(), notification.getMessage());
    }

    /**
     * Process scheduled notifications
     */
    @Transactional
    public void processScheduledNotifications() {
        List<Notification> scheduled = notificationRepository.findPendingNotifications(
                NotificationStatus.SCHEDULED,
                LocalDateTime.now()
        );

        log.info("Processing {} scheduled notifications", scheduled.size());

        for (Notification notification : scheduled) {
            sendNotification(notification);
        }
    }

    /**
     * Retry failed notifications
     */
    @Transactional
    public void retryFailedNotifications(int maxRetries) {
        List<Notification> failed = notificationRepository.findFailedNotificationsForRetry(maxRetries);

        log.info("Retrying {} failed notifications", failed.size());

        for (Notification notification : failed) {
            sendNotification(notification);
        }
    }

    /**
     * Get notifications for a recipient
     */
    public List<NotificationResponse> getNotificationsByRecipient(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get notification by ID
     */
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return mapToResponse(notification);
    }

    /**
     * Cancel scheduled notification
     */
    @Transactional
    public void cancelNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));

        if (notification.getStatus() != NotificationStatus.SENT) {
            notification.setStatus(NotificationStatus.CANCELLED);
            notificationRepository.save(notification);
            log.info("Notification cancelled: {}", id);
        } else {
            throw new IllegalStateException("Cannot cancel already sent notification");
        }
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipientId())
                .recipientEmail(notification.getRecipientEmail())
                .recipientPhone(notification.getRecipientPhone())
                .recipientName(notification.getRecipientName())
                .notificationType(notification.getNotificationType())
                .channel(notification.getChannel())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .scheduledTime(notification.getScheduledTime())
                .sentTime(notification.getSentTime())
                .errorMessage(notification.getErrorMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
