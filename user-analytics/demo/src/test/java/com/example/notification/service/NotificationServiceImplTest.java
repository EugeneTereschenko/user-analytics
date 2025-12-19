/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */
package com.example.notification.service;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Profile;
import com.example.demo.model.User;
import com.example.notification.repository.ProfileNotificationRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.UserService;
import com.example.notification.dto.NotificationsDTO;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.repository.UserNotificationReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationServiceImpl Tests")
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ProfileNotificationRepository profileNotificationRepository;
    @Mock
    private UserNotificationReadRepository readRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        lenient().when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));
    }


    @Test
    @DisplayName("Should fetch all notifications for current user")
    void getAllNotifications_returnsNotifications() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setTitle("Test");
        notification.setMessage("Test message");
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(NotificationType.INFO);
        notification.setPriority(NotificationPriority.LOW);

        when(notificationRepository.findNotificationsByUserId(1L)).thenReturn(List.of(notification));
        when(readRepository.findByUserIdAndNotificationId(1L, 1L)).thenReturn(Optional.empty());

        List<NotificationsDTO> result = notificationService.getAllNotifications();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Should fetch unread notifications")
    void getUnreadNotifications_returnsUnread() {
        Notification notification = new Notification();
        notification.setId(2L);
        notification.setTitle("Unread");
        notification.setMessage("Unread message");
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(NotificationType.INFO);
        notification.setPriority(NotificationPriority.HIGH);

        when(notificationRepository.findNotificationsByUserId(1L)).thenReturn(List.of(notification));
        when(readRepository.existsByUserIdAndNotificationId(1L, 2L)).thenReturn(false);
        when(readRepository.findByUserIdAndNotificationId(1L, 2L)).thenReturn(Optional.empty());

        List<NotificationsDTO> result = notificationService.getUnreadNotifications();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Unread");
    }

    @Test
    @DisplayName("Should create notification if title is unique")
    void createNotification_success() {
        NotificationsDTO dto = NotificationsDTO.builder()
                .title("New Notification")
                .message("Message")
                .timestamp("2024-06-01T12:00:00")
                .type(NotificationType.INFO)
                .priority(NotificationPriority.LOW)
                .build();

        when(notificationRepository.findByTitle("New Notification")).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(10L);
            return n;
        });
        Profile profile = new Profile();
        profile.setId(5L);
        when(profileRepository.findProfilesByUserId(1L)).thenReturn(List.of(profile));

        ResponseDTO response = notificationService.createNotification(dto);

        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).contains("created");
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should not create notification if title exists")
    void createNotification_duplicateTitle() {
        NotificationsDTO dto = NotificationsDTO.builder()
                .title("Duplicate")
                .message("Message")
                .timestamp("2024-06-01T12:00:00")
                .type(NotificationType.INFO)
                .priority(NotificationPriority.LOW)
                .build();

        when(notificationRepository.findByTitle("Duplicate")).thenReturn(Optional.of(new Notification()));

        ResponseDTO response = notificationService.createNotification(dto);

        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("already exists");
        verify(notificationRepository, never()).save(any());
    }

}