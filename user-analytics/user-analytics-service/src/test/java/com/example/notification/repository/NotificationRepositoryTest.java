/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.repository;

import com.example.demo.UserAnalyticsJavaApplication;
import com.example.demo.dto.UserEventDTO;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

    @Test
    @DisplayName("Should save and find notification by title")
    void saveAndFindByTitle() {
        Notification notification = new Notification();
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setType(NotificationType.INFO);
        notification.setPriority(NotificationPriority.LOW);
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));


        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findByTitle("Test Title");
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("Test Message");
    }

    @Test
    @DisplayName("Should find notifications by type")
    void findByType() {
        Notification notification = new Notification();
        notification.setTitle("Type Test");
        notification.setMessage("Type Message");
        notification.setType(NotificationType.INFO);
        notification.setPriority(NotificationPriority.HIGH);
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        notificationRepository.save(notification);

        List<Notification> found = notificationRepository.findByType(NotificationType.INFO);
        assertThat(found).extracting(Notification::getTitle).contains("Type Test");
    }

    @Test
    @DisplayName("Should search notifications by query")
    void searchNotifications() {
        Notification notification = new Notification();
        notification.setTitle("Searchable Title");
        notification.setMessage("Some message");
        notification.setType(NotificationType.INFO);
        notification.setPriority(NotificationPriority.MEDIUM);
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        notificationRepository.save(notification);

        List<Notification> found = notificationRepository.searchNotifications("searchable");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getTitle()).containsIgnoringCase("searchable");
    }

}