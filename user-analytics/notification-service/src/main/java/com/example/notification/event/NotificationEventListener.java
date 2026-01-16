/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.event;

import com.example.notification.dto.AppointmentEventDTO;
import com.example.notification.dto.NotificationRequest;
import com.example.notification.dto.PrescriptionEventDTO;
import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationType;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    /**
     * Listen for appointment events
     */
    @KafkaListener(topics = "appointment-events", groupId = "notification-service")
    public void handleAppointmentEvent(AppointmentEventDTO event) {
        log.info("Received appointment event: {} for patient: {}", event.getEventType(), event.getPatientName());

        try {
            switch (event.getEventType()) {
                case "CREATED" -> sendAppointmentConfirmation(event);
                case "REMINDER" -> sendAppointmentReminder(event);
                case "CANCELLED" -> sendAppointmentCancellation(event);
                case "UPDATED" -> sendAppointmentUpdate(event);
            }
        } catch (Exception e) {
            log.error("Failed to process appointment event: {}", e.getMessage());
        }
    }

    /**
     * Listen for prescription events
     */
    @KafkaListener(topics = "prescription-events", groupId = "notification-service")
    public void handlePrescriptionEvent(PrescriptionEventDTO event) {
        log.info("Received prescription event: {} for patient: {}", event.getEventType(), event.getPatientName());

        try {
            switch (event.getEventType()) {
                case "CREATED" -> sendPrescriptionCreated(event);
                case "READY" -> sendPrescriptionReady(event);
                case "REMINDER" -> sendPrescriptionReminder(event);
            }
        } catch (Exception e) {
            log.error("Failed to process prescription event: {}", e.getMessage());
        }
    }

    /**
     * Listen for test result events
     */
    @KafkaListener(topics = "test-result-events", groupId = "notification-service")
    public void handleTestResultEvent(AppointmentEventDTO event) {
        log.info("Received test result event for patient: {}", event.getPatientName());

        try {
            sendTestResultAlert(event);
        } catch (Exception e) {
            log.error("Failed to process test result event: {}", e.getMessage());
        }
    }

    private void sendAppointmentConfirmation(AppointmentEventDTO event) {
        String message = String.format(
                "Dear %s, your appointment with Dr. %s has been confirmed for %s.",
                event.getPatientName(),
                event.getDoctorName(),
                event.getAppointmentTime().format(formatter)
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.APPOINTMENT_CONFIRMATION)
                .channel(NotificationChannel.BOTH)
                .subject("Appointment Confirmed")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendAppointmentReminder(AppointmentEventDTO event) {
        String message = String.format(
                "Reminder: You have an appointment with Dr. %s tomorrow at %s. Please arrive 15 minutes early.",
                event.getDoctorName(),
                event.getAppointmentTime().format(formatter)
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.APPOINTMENT_REMINDER)
                .channel(NotificationChannel.BOTH)
                .subject("Appointment Reminder")
                .message(message)
                .scheduledTime(event.getAppointmentTime().minusDays(1))
                .build();

        notificationService.createNotification(request);
    }

    private void sendAppointmentCancellation(AppointmentEventDTO event) {
        String message = String.format(
                "Dear %s, your appointment with Dr. %s scheduled for %s has been cancelled. Please contact us to reschedule.",
                event.getPatientName(),
                event.getDoctorName(),
                event.getAppointmentTime().format(formatter)
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.APPOINTMENT_CANCELLATION)
                .channel(NotificationChannel.BOTH)
                .subject("Appointment Cancelled")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendAppointmentUpdate(AppointmentEventDTO event) {
        String message = String.format(
                "Dear %s, your appointment with Dr. %s has been updated. New time: %s",
                event.getPatientName(),
                event.getDoctorName(),
                event.getAppointmentTime().format(formatter)
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.APPOINTMENT_REMINDER)
                .channel(NotificationChannel.EMAIL)
                .subject("Appointment Updated")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendPrescriptionCreated(PrescriptionEventDTO event) {
        String message = String.format(
                "Dear %s, a new prescription for %s (%s) has been created. You can pick it up from the pharmacy.",
                event.getPatientName(),
                event.getMedicationName(),
                event.getDosage()
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.PRESCRIPTION_REMINDER)
                .channel(NotificationChannel.EMAIL)
                .subject("New Prescription")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendPrescriptionReady(PrescriptionEventDTO event) {
        String message = String.format(
                "Dear %s, your prescription for %s is ready for pickup.",
                event.getPatientName(),
                event.getMedicationName()
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.PRESCRIPTION_READY)
                .channel(NotificationChannel.BOTH)
                .subject("Prescription Ready")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendPrescriptionReminder(PrescriptionEventDTO event) {
        String message = String.format(
                "Reminder: Take your medication - %s (%s)",
                event.getMedicationName(),
                event.getDosage()
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.PRESCRIPTION_REMINDER)
                .channel(NotificationChannel.SMS)
                .subject("Medication Reminder")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }

    private void sendTestResultAlert(AppointmentEventDTO event) {
        String message = String.format(
                "Dear %s, your test results are now available. Please log in to your patient portal to view them.",
                event.getPatientName()
        );

        NotificationRequest request = NotificationRequest.builder()
                .recipientId(event.getPatientId())
                .recipientEmail(event.getPatientEmail())
                .recipientPhone(event.getPatientPhone())
                .recipientName(event.getPatientName())
                .notificationType(NotificationType.TEST_RESULT_ALERT)
                .channel(NotificationChannel.BOTH)
                .subject("Test Results Available")
                .message(message)
                .build();

        notificationService.createNotification(request);
    }
}