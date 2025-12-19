/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.service;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileNotification;
import com.example.demo.model.User;
import com.example.demo.repository.*;
import com.example.demo.service.UserService;
import com.example.demo.util.DateTimeConverter;
import com.example.notification.dto.NotificationsDTO;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import com.example.notification.model.UserNotificationRead;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.repository.ProfileNotificationRepository;
import com.example.notification.repository.UserNotificationReadRepository;
import com.example.notification.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ProfileNotificationRepository profileNotificationRepository;
    private final UserNotificationReadRepository readRepository;

    @Override
    public List<NotificationsDTO> getAllNotifications() {
        log.info("Fetching all notifications for current user");
        Long userId = getCurrentUserId();

        return notificationRepository.findNotificationsByUserId(userId)
                .stream()
                .map(notification -> convertToDTO(notification, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationsDTO> getUnreadNotifications() {
        log.info("Fetching unread notifications");
        Long userId = getCurrentUserId();

        return notificationRepository.findNotificationsByUserId(userId)
                .stream()
                .filter(notification -> !isRead(userId, notification.getId()))
                .map(notification -> convertToDTO(notification, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationsDTO> getNotificationsByType(NotificationType type) {
        log.info("Fetching notifications by type: {}", type);
        Long userId = getCurrentUserId();

        return notificationRepository.findByType(type)
                .stream()
                .map(notification -> convertToDTO(notification, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationsDTO> getNotificationsByPriority(NotificationPriority priority) {
        log.info("Fetching notifications by priority: {}", priority);
        Long userId = getCurrentUserId();

        return notificationRepository.findByPriority(priority)
                .stream()
                .map(notification -> convertToDTO(notification, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationsDTO> searchNotifications(String query) {
        log.info("Searching notifications with query: {}", query);
        Long userId = getCurrentUserId();

        return notificationRepository.searchNotifications(query)
                .stream()
                .map(notification -> convertToDTO(notification, userId))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ResponseDTO createNotification(NotificationsDTO notificationsDTO) {
        log.info("Creating notification: {}", notificationsDTO.getTitle());

        if (notificationRepository.findByTitle(notificationsDTO.getTitle()).isPresent()) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Notification with this title already exists")
                    .build();
        }

        Notification notification = new Notification.Builder()
                .title(notificationsDTO.getTitle())
                .message(notificationsDTO.getMessage())
                .timestamp(convertStringToTimestamp(notificationsDTO.getTimestamp()))
                .type(notificationsDTO.getType())
                .priority(notificationsDTO.getPriority())
                .category(notificationsDTO.getCategory())
                .actionUrl(notificationsDTO.getActionUrl())
                .sender(notificationsDTO.getSender())
                .metadata(notificationsDTO.getMetadata())
                .build();

        Notification saved = notificationRepository.save(notification);
        saveProfileNotification(saved.getId());

        return ResponseDTO.builder()
                .status("success")
                .message("Notification created successfully")
                .build();
    }

    @Transactional
    @Override
    public void markAsRead(Long notificationId) {
        Long userId = getCurrentUserId();
        log.info("Marking notification {} as read for user {}", notificationId, userId);

        if (!readRepository.existsByUserIdAndNotificationId(userId, notificationId)) {
            UserNotificationRead read = new UserNotificationRead();
            read.setUserId(userId);
            read.setNotificationId(notificationId);
            readRepository.save(read);
        }
    }

    @Transactional
    @Override
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        log.info("Marking all notifications as read for user {}", userId);

        List<Notification> notifications = notificationRepository.findNotificationsByUserId(userId);
        for (Notification notification : notifications) {
            if (!readRepository.existsByUserIdAndNotificationId(userId, notification.getId())) {
                UserNotificationRead read = new UserNotificationRead();
                read.setUserId(userId);
                read.setNotificationId(notification.getId());
                readRepository.save(read);
            }
        }
    }

    @Transactional
    @Override
    public void deleteNotification(Long notificationId) {
        log.info("Deleting notification: {}", notificationId);
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    @Override
    public void deleteAllReadNotifications() {
        Long userId = getCurrentUserId();
        log.info("Deleting all read notifications for user {}", userId);

        List<UserNotificationRead> readNotifications = readRepository.findByUserId(userId);
        List<Long> readNotificationIds = readNotifications.stream()
                .map(UserNotificationRead::getNotificationId)
                .collect(Collectors.toList());

        notificationRepository.deleteAllById(readNotificationIds);
    }

    @Override
    public Long getUnreadCount() {
        Long userId = getCurrentUserId();
        return notificationRepository.findNotificationsByUserId(userId)
                .stream()
                .filter(notification -> !isRead(userId, notification.getId()))
                .count();
    }

    private NotificationsDTO convertToDTO(Notification notification, Long userId) {
        UserNotificationRead readStatus = readRepository
                .findByUserIdAndNotificationId(userId, notification.getId())
                .orElse(null);

        return NotificationsDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .timestamp(notification.getTimestamp().toString())
                .type(notification.getType())
                .priority(notification.getPriority())
                .isRead(readStatus != null)
                .readAt(readStatus != null ? readStatus.getReadAt().toString() : null)
                .category(notification.getCategory())
                .actionUrl(notification.getActionUrl())
                .sender(notification.getSender())
                .metadata(notification.getMetadata())
                .build();
    }

    private boolean isRead(Long userId, Long notificationId) {
        return readRepository.existsByUserIdAndNotificationId(userId, notificationId);
    }

    private Long getCurrentUserId() {
        return userService.getAuthenticatedUser()
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }

    private void saveProfileNotification(Long notificationId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileNotification profileNotification = ProfileNotification.builder()
                .profileId(profile.getId())
                .notificationId(notificationId)
                .build();

        profileNotificationRepository.saveAndFlush(profileNotification);
    }


    public static Timestamp convertStringToTimestamp(String dateTimeStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, FORMATTER);
        return Timestamp.valueOf(localDateTime);
    }

}