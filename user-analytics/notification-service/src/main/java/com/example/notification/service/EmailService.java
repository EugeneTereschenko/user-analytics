/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Send simple text email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Simple email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send HTML email with template
     */
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("HTML email sent to: {} using template: {}", to, templateName);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Send appointment reminder email
     */
    public void sendAppointmentReminder(String to, String patientName, String doctorName, String appointmentTime) {
        Map<String, Object> variables = Map.of(
                "patientName", patientName,
                "doctorName", doctorName,
                "appointmentTime", appointmentTime
        );

        sendHtmlEmail(to, "Appointment Reminder", "appointment-reminder", variables);
    }

    /**
     * Send test result alert email
     */
    public void sendTestResultAlert(String to, String patientName, String testName) {
        Map<String, Object> variables = Map.of(
                "patientName", patientName,
                "testName", testName
        );

        sendHtmlEmail(to, "Test Results Available", "test-result-alert", variables);
    }

    /**
     * Send prescription reminder email
     */
    public void sendPrescriptionReminder(String to, String patientName, String medicationName, String dosage) {
        Map<String, Object> variables = Map.of(
                "patientName", patientName,
                "medicationName", medicationName,
                "dosage", dosage
        );

        sendHtmlEmail(to, "Prescription Reminder", "prescription-reminder", variables);
    }

    /**
     * Send appointment confirmation email
     */
    public void sendAppointmentConfirmation(String to, String patientName, String doctorName, String appointmentTime) {
        Map<String, Object> variables = Map.of(
                "patientName", patientName,
                "doctorName", doctorName,
                "appointmentTime", appointmentTime
        );

        sendHtmlEmail(to, "Appointment Confirmed", "appointment-confirmation", variables);
    }
}