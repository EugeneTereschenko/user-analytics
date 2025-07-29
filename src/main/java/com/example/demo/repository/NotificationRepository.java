package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByTitle(String title);

    List<Notification> findAllByOrderByTimestampDesc();

    @Query("SELECT new com.example.demo.model.Notification(no.id, no.title, no.message, no.timestamp) " +
            "FROM Notification no " +
            "JOIN ProfileNotification pn ON pn.notificationId = no.id " +
            "JOIN UserProfile up ON pn.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY no.timestamp DESC")
    List<Notification> findNotificationsByUserId(@Param("userId") Long userId);
}
