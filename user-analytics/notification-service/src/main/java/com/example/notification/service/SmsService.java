/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Slf4j
@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio SMS service initialized");
    }

    /**
     * Send SMS message
     */
    public void sendSms(String to, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody
            ).create();

            log.info("SMS sent to: {} with SID: {}", to, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    /**
     * Send appointment reminder SMS
     */
    public void sendAppointmentReminderSms(String to, String patientName, String doctorName, String appointmentTime) {
        String message = String.format(
                "Hi %s, this is a reminder for your appointment with Dr. %s on %s. Reply CONFIRM to confirm.",
                patientName, doctorName, appointmentTime
        );
        sendSms(to, message);
    }

    /**
     * Send test result alert SMS
     */
    public void sendTestResultAlertSms(String to, String patientName) {
        String message = String.format(
                "Hi %s, your test results are now available. Please log in to your patient portal to view them.",
                patientName
        );
        sendSms(to, message);
    }

    /**
     * Send prescription reminder SMS
     */
    public void sendPrescriptionReminderSms(String to, String patientName, String medicationName) {
        String message = String.format(
                "Hi %s, reminder to take your medication: %s. Stay healthy!",
                patientName, medicationName
        );
        sendSms(to, message);
    }

    /**
     * Send appointment confirmation SMS
     */
    public void sendAppointmentConfirmationSms(String to, String patientName, String appointmentTime) {
        String message = String.format(
                "Hi %s, your appointment has been confirmed for %s. See you then!",
                patientName, appointmentTime
        );
        sendSms(to, message);
    }
}
