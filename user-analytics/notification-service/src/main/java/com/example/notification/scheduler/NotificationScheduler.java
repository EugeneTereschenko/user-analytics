/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.scheduler;

import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;

    /**
     * Process scheduled notifications every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void processScheduledNotifications() {
        log.info("Running scheduled notification processor");
        try {
            notificationService.processScheduledNotifications();
        } catch (Exception e) {
            log.error("Error processing scheduled notifications: {}", e.getMessage());
        }
    }

    /**
     * Retry failed notifications every 30 minutes
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes
    public void retryFailedNotifications() {
        log.info("Running failed notification retry");
        try {
            notificationService.retryFailedNotifications(3); // Max 3 retries
        } catch (Exception e) {
            log.error("Error retrying failed notifications: {}", e.getMessage());
        }
    }
}
