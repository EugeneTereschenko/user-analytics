package com.example.demo.service;

import com.example.demo.dto.NotificationsDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.*;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ProfileNotificationRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.impl.NotificationService;
import com.example.demo.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ProfileNotificationRepository profileNotificationRepository;

    public Notification saveNotification(NotificationsDTO notificationsDTO) {
        Notification notification = new Notification.Builder()
                .title(notificationsDTO.getTitle())
                .message(notificationsDTO.getMessage())
                .timestamp(DateTimeConverter.convertStringToTimestamp(notificationsDTO.getTimestamp()))
                .build();
        return notificationRepository.save(notification);
    }


    public ResponseDTO createNotification(NotificationsDTO notificationsDTO) {
        if (notificationRepository.findByTitle(notificationsDTO.getTitle()).isPresent()) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Notification with this title already exists")
                    .build();
        }
        Notification existingNotification = notificationRepository.save(new Notification.Builder()
                .title(notificationsDTO.getTitle())
                .message(notificationsDTO.getMessage())
                .timestamp(DateTimeConverter.convertStringToTimestamp(notificationsDTO.getTimestamp()))
                .build());
        saveProfileNotification(existingNotification.getId());
        return ResponseDTO.builder()
                .status("success")
                .message("Notification created successfully")
                .build();
    }

    public List<NotificationsDTO> getAllNotifications() {

        return notificationRepository.findNotificationsByUserId(userService.getAuthenticatedUser().get().getId())
                .stream()
                .map(notification -> NotificationsDTO.builder()
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .timestamp(DateTimeConverter.convertTimestampToString(notification.getTimestamp()))
                        .build())
                .toList();
    }

    private Boolean saveProfileNotification(Long notificationId) {
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
        return true; // Assuming the save operation is successful
    }
}
