/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.repository;

import com.example.notification.model.Notification;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientId(Long recipientId);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByNotificationType(NotificationType type);

    @Query("SELECT n FROM Notification n WHERE n.status = :status AND n.scheduledTime <= :currentTime")
    List<Notification> findPendingNotifications(
            @Param("status") NotificationStatus status,
            @Param("currentTime") LocalDateTime currentTime
    );

    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED' AND n.retryCount < :maxRetries")
    List<Notification> findFailedNotificationsForRetry(@Param("maxRetries") int maxRetries);

    List<Notification> findByRecipientIdAndStatusOrderByCreatedAtDesc(
            Long recipientId,
            NotificationStatus status
    );

    @Query("SELECT n FROM Notification n WHERE n.recipientId = :recipientId " +
            "AND n.createdAt BETWEEN :startDate AND :endDate")
    List<Notification> findByRecipientAndDateRange(
            @Param("recipientId") Long recipientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
