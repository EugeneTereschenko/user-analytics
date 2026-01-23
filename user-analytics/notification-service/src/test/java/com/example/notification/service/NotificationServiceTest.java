package com.example.notification.service;

import com.example.notification.dto.NotificationRequest;
import com.example.notification.dto.NotificationResponse;
import com.example.notification.model.Notification;
import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.testutil.NotificationRequestTestBuilder;
import com.example.notification.testutil.TestcontainersConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Import({TestcontainersConfiguration.class})
@SpringBootTest
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Notification buildNotification(NotificationChannel channel, NotificationStatus status) {
        return Notification.builder()
                .id(42L)
                .recipientId(1L)
                .recipientEmail("test@example.com")
                .recipientPhone("123456789")
                .recipientName("Test User")
                .notificationType(NotificationType.APPOINTMENT_REMINDER)
                .channel(channel)
                .subject("Reminder")
                .message("This is your reminder")
                .scheduledTime(null)
                .status(status)
                .createdAt(LocalDateTime.now())
                .retryCount(0)
                .build();
    }

/*
    @Test
    void testCreateNotification_EmailChannel_SendsEmail() throws Exception {
        NotificationRequest request = new NotificationRequestTestBuilder()
                .withChannel(NotificationChannel.EMAIL)
                .withScheduledTime(null)
                .withNotificationType(NotificationType.APPOINTMENT_REMINDER)
                .build();

        when(notificationRepository.save(any())).thenAnswer(inv -> {
            Notification n = inv.getArgument(0);
            if (n.getId() == null) {
                // Simulate JPA assign ID
                n = Notification.builder()
                        .id(42L)
                        .recipientId(n.getRecipientId())
                        .recipientEmail(n.getRecipientEmail())
                        .recipientPhone(n.getRecipientPhone())
                        .recipientName(n.getRecipientName())
                        .notificationType(n.getNotificationType())
                        .channel(n.getChannel())
                        .subject(n.getSubject())
                        .message(n.getMessage())
                        .scheduledTime(n.getScheduledTime())
                        .status(n.getStatus())
                        .createdAt(LocalDateTime.now())
                        .retryCount(n.getRetryCount())
                        .build();
            }
            return n;
        });
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"foo\":\"bar\"}");

        NotificationResponse response = notificationService.createNotification(request);

        assertNotNull(response);
        verify(emailService, times(1)).sendSimpleEmail(anyString(), anyString(), anyString());
        verify(notificationRepository, times(2)).save(any()); // Initial and after send
    }

    @Test
    void testCreateNotification_Scheduled_DoesNotSend() throws Exception {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        NotificationRequest request = new NotificationRequestTestBuilder()
                .withChannel(NotificationChannel.EMAIL)
                .withScheduledTime(futureTime)
                .withNotificationType(NotificationType.APPOINTMENT_REMINDER)
                .build();

        when(notificationRepository.save(any())).thenAnswer(inv -> {
            Notification n = inv.getArgument(0);
            if (n.getId() == null) {
                n = Notification.builder()
                        .id(42L)
                        .recipientId(n.getRecipientId())
                        .recipientEmail(n.getRecipientEmail())
                        .recipientPhone(n.getRecipientPhone())
                        .recipientName(n.getRecipientName())
                        .notificationType(n.getNotificationType())
                        .channel(n.getChannel())
                        .subject(n.getSubject())
                        .message(n.getMessage())
                        .scheduledTime(n.getScheduledTime())
                        .status(n.getStatus())
                        .createdAt(LocalDateTime.now())
                        .retryCount(n.getRetryCount())
                        .build();
            }
            return n;
        });
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"foo\":\"bar\"}");

        NotificationResponse response = notificationService.createNotification(request);

        assertNotNull(response);
        verify(emailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendNotification_Email_Success() {
        Notification notification = buildNotification(NotificationChannel.EMAIL, NotificationStatus.PENDING);

        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        notificationService.sendNotification(notification);

        verify(emailService, times(1)).sendSimpleEmail(anyString(), anyString(), anyString());
        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    void testSendNotification_Sms_Success() {
        Notification notification = buildNotification(NotificationChannel.SMS, NotificationStatus.PENDING);

        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        notificationService.sendNotification(notification);

        verify(smsService, times(1)).sendSms(anyString(), anyString());
        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    void testSendNotification_Failed() {
        Notification notification = buildNotification(NotificationChannel.EMAIL, NotificationStatus.PENDING);
        doThrow(new RuntimeException("fail")).when(emailService).sendSimpleEmail(anyString(), anyString(), anyString());
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        notificationService.sendNotification(notification);

        assertEquals(NotificationStatus.FAILED, notification.getStatus());
        assertEquals(1, notification.getRetryCount());
    }

    @Test
    void testProcessScheduledNotifications_SendsAll() {
        Notification scheduled = buildNotification(NotificationChannel.EMAIL, NotificationStatus.SCHEDULED);

        when(notificationRepository.findPendingNotifications(eq(NotificationStatus.SCHEDULED), any()))
                .thenReturn(List.of(scheduled));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        notificationService.processScheduledNotifications();

        verify(emailService, times(1)).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testRetryFailedNotifications_SendsAll() {
        Notification failed = buildNotification(NotificationChannel.SMS, NotificationStatus.FAILED);

        when(notificationRepository.findFailedNotificationsForRetry(anyInt()))
                .thenReturn(List.of(failed));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        notificationService.retryFailedNotifications(3);

        verify(smsService, times(1)).sendSms(anyString(), anyString());
    }

    @Test
    void testGetNotificationsByRecipient() {
        Notification n = buildNotification(NotificationChannel.EMAIL, NotificationStatus.SENT);
        when(notificationRepository.findByRecipientId(1L)).thenReturn(List.of(n));

        List<NotificationResponse> responses = notificationService.getNotificationsByRecipient(1L);

        assertEquals(1, responses.size());
        assertEquals(n.getId(), responses.get(0).getId());
    }

    @Test
    void testGetNotificationById_Found() {
        Notification n = buildNotification(NotificationChannel.SMS, NotificationStatus.SENT);
        when(notificationRepository.findById(42L)).thenReturn(Optional.of(n));

        NotificationResponse response = notificationService.getNotificationById(42L);

        assertEquals(42L, response.getId());
    }
*/

    @Test
    void testGetNotificationById_NotFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> notificationService.getNotificationById(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }

/*    @Test
    void testCancelNotification_Success() {
        Notification n = buildNotification(NotificationChannel.EMAIL, NotificationStatus.SCHEDULED);
        when(notificationRepository.findById(42L)).thenReturn(Optional.of(n));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> notificationService.cancelNotification(42L));
        assertEquals(NotificationStatus.CANCELLED, n.getStatus());
    }

    @Test
    void testCancelNotification_AlreadySent() {
        Notification n = buildNotification(NotificationChannel.EMAIL, NotificationStatus.SENT);
        when(notificationRepository.findById(42L)).thenReturn(Optional.of(n));

        assertThrows(IllegalStateException.class, () -> notificationService.cancelNotification(42L));
    }*/

    @Test
    void testCancelNotification_NotFound() {
        when(notificationRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.cancelNotification(42L));
    }
}