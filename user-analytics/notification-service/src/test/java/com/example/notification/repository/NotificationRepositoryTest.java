package com.example.notification.repository;

import com.example.common.security.client.AuthServiceClient;
import com.example.notification.NotificationServiceApplication;
import com.example.notification.controller.NotificationController;
import com.example.notification.dto.AppointmentEventDTO;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;
import com.example.notification.service.EmailService;
import com.example.notification.service.NotificationService;
import com.example.notification.testutil.NoKafkaListenerConfig;
import com.example.notification.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class
})
@Import({TestcontainersConfiguration.class, NoKafkaListenerConfig.class})
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private AuthServiceClient authServiceClient;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private EmailService emailService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationController notificationController;

    @MockBean
    private KafkaTemplate<String, AppointmentEventDTO> kafkaTemplate;

    @MockBean
    private MessageListenerContainer messageListenerContainer;


    @MockBean(name = "kafkaListenerContainerFactory")
    private KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;

    @MockBean(name = "appointmentKafkaListenerContainerFactory")
    private KafkaListenerContainerFactory<?> appointmentKafkaListenerContainerFactory;

    @MockBean(name = "prescriptionKafkaListenerContainerFactory")
    private KafkaListenerContainerFactory<?> prescriptionKafkaListenerContainerFactory;

/*
    @Test
    @DisplayName("Should find notifications by recipientId")
    void testFindByRecipientId() {
        Notification notification = new Notification();
        notification.setRecipientId(1L);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setNotificationType(NotificationType.BILLING_NOTIFICATION);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByRecipientId(1L);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getRecipientId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should find notifications by status")
    void testFindByStatus() {
        Notification notification = new Notification();
        notification.setRecipientId(2L);
        notification.setStatus(NotificationStatus.SENT);
        notification.setNotificationType(NotificationType.GENERAL_NOTIFICATION);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByStatus(NotificationStatus.SENT);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getStatus()).isEqualTo(NotificationStatus.SENT);
    }

    @Test
    @DisplayName("Should find pending notifications")
    void testFindPendingNotifications() {
        Notification notification = new Notification();
        notification.setRecipientId(3L);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setNotificationType(NotificationType.TEST_RESULT_ALERT);
        notification.setScheduledTime(LocalDateTime.now().minusMinutes(1));
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findPendingNotifications(
                NotificationStatus.PENDING, LocalDateTime.now());
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should find failed notifications for retry")
    void testFindFailedNotificationsForRetry() {
        Notification notification = new Notification();
        notification.setRecipientId(4L);
        notification.setStatus(NotificationStatus.FAILED);
        notification.setNotificationType(NotificationType.APPOINTMENT_CANCELLATION);
        notification.setRetryCount(1);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findFailedNotificationsForRetry(3);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should find by recipientId and status ordered by createdAt desc")
    void testFindByRecipientIdAndStatusOrderByCreatedAtDesc() {
        Notification notification = new Notification();
        notification.setRecipientId(5L);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setNotificationType(NotificationType.TEST_RESULT_ALERT);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByRecipientIdAndStatusOrderByCreatedAtDesc(
                5L, NotificationStatus.PENDING);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should find by recipient and date range")
    void testFindByRecipientAndDateRange() {
        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification();
        notification.setRecipientId(6L);
        notification.setStatus(NotificationStatus.SENT);
        notification.setNotificationType(NotificationType.GENERAL_NOTIFICATION);
        notification.setCreatedAt(now.minusDays(1));
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByRecipientAndDateRange(
                6L, now.minusDays(2), now);
        assertThat(result).isNotEmpty();
    }*/
}