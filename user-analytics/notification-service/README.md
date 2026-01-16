# Notification Service

Comprehensive notification service for healthcare system supporting Email, SMS, appointment reminders, test result alerts, and prescription reminders.

## Features

- ✉️ **Email Notifications** - HTML templated emails using Thymeleaf
- 📱 **SMS Notifications** - SMS support via Twilio
- 📅 **Appointment Reminders** - Automated appointment reminders 24 hours before
- 🧪 **Test Result Alerts** - Notify patients when test results are ready
- 💊 **Prescription Reminders** - Medication reminders and pickup notifications
- 🔄 **Event-Driven** - Listens to Kafka events from other services
- ⏰ **Scheduled Processing** - Automatic processing of scheduled notifications
- 🔁 **Retry Logic** - Automatic retry for failed notifications
- 📊 **Status Tracking** - Track notification delivery status

## Architecture

```
appointment-service → Kafka → notification-service → Email/SMS
prescription-service → Kafka → notification-service → Email/SMS
test-result-service → Kafka → notification-service → Email/SMS
```

## Prerequisites

- Java 21
- PostgreSQL
- Kafka
- Gmail account (for email) or SMTP server
- Twilio account (for SMS)

## Configuration

### 1. Email Setup (Gmail)

1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password: [Google Account Settings](https://myaccount.google.com/apppasswords)
3. Update `application.yml`:

```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-16-digit-app-password
```

### 2. Twilio Setup (SMS)

1. Create a Twilio account: [https://www.twilio.com](https://www.twilio.com)
2. Get your Account SID, Auth Token, and Phone Number
3. Update `application.yml`:

```yaml
twilio:
  account-sid: ACxxxxxxxxxxxxxxxxx
  auth-token: your-auth-token
  phone-number: +1234567890
```

### 3. Database Setup

```sql
CREATE DATABASE notificationdb;
```

## Installation

### 1. Add to Parent POM

```xml
<modules>
    <module>notification-service</module>
</modules>
```

### 2. Create Directory Structure

```
notification-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/notification/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── event/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── scheduler/
│   │   │   ├── service/
│   │   │   └── NotificationServiceApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── appointment-reminder.html
│   │       │   ├── appointment-confirmation.html
│   │       │   ├── test-result-alert.html
│   │       │   └── prescription-reminder.html
│   │       └── application.yml
```

### 3. Build

```bash
mvn clean install
```

### 4. Run

```bash
mvn spring-boot:run
```

## API Endpoints

### Create Notification

```bash
POST /api/notifications
Content-Type: application/json

{
  "recipientId": 1,
  "recipientEmail": "patient@example.com",
  "recipientPhone": "+1234567890",
  "recipientName": "John Doe",
  "notificationType": "APPOINTMENT_REMINDER",
  "channel": "BOTH",
  "subject": "Appointment Reminder",
  "message": "Your appointment is tomorrow",
  "scheduledTime": "2026-01-20T10:00:00"
}
```

### Get Notifications for Recipient

```bash
GET /api/notifications/recipient/{recipientId}
```

### Get Notification by ID

```bash
GET /api/notifications/{id}
```

### Cancel Notification

```bash
DELETE /api/notifications/{id}
```

### Manually Process Scheduled

```bash
POST /api/notifications/process-scheduled
```

### Manually Retry Failed

```bash
POST /api/notifications/retry-failed
```

## Kafka Events

### Appointment Events

**Topic:** `appointment-events`

```json
{
  "appointmentId": 1,
  "patientId": 1,
  "patientName": "John Doe",
  "patientEmail": "john@example.com",
  "patientPhone": "+1234567890",
  "doctorId": 1,
  "doctorName": "Dr. Smith",
  "appointmentTime": "2026-01-20T10:00:00",
  "eventType": "CREATED",
  "status": "CONFIRMED"
}
```

**Event Types:**
- `CREATED` - Send confirmation
- `REMINDER` - Send reminder 24h before
- `CANCELLED` - Send cancellation notice
- `UPDATED` - Send update notice

### Prescription Events

**Topic:** `prescription-events`

```json
{
  "prescriptionId": 1,
  "patientId": 1,
  "patientName": "John Doe",
  "patientEmail": "john@example.com",
  "patientPhone": "+1234567890",
  "medicationName": "Amoxicillin",
  "dosage": "500mg",
  "startDate": "2026-01-15T00:00:00",
  "endDate": "2026-01-25T00:00:00",
  "eventType": "CREATED"
}
```

**Event Types:**
- `CREATED` - New prescription notification
- `READY` - Prescription ready for pickup
- `REMINDER` - Medication reminder

### Test Result Events

**Topic:** `test-result-events`

```json
{
  "appointmentId": 1,
  "patientId": 1,
  "patientName": "John Doe",
  "patientEmail": "john@example.com",
  "patientPhone": "+1234567890",
  "eventType": "RESULTS_READY"
}
```

## Scheduled Jobs

### Process Scheduled Notifications
- **Frequency:** Every 5 minutes
- **Action:** Sends notifications that are due

### Retry Failed Notifications
- **Frequency:** Every 30 minutes
- **Action:** Retries failed notifications (max 3 attempts)

## Testing

### Test Email Notification

```bash
curl -X POST http://localhost:8089/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": 1,
    "recipientEmail": "test@example.com",
    "recipientName": "Test User",
    "notificationType": "GENERAL_NOTIFICATION",
    "channel": "EMAIL",
    "subject": "Test Email",
    "message": "This is a test email notification"
  }'
```

### Test SMS Notification

```bash
curl -X POST http://localhost:8089/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": 1,
    "recipientPhone": "+1234567890",
    "recipientName": "Test User",
    "notificationType": "GENERAL_NOTIFICATION",
    "channel": "SMS",
    "message": "This is a test SMS notification"
  }'
```

### Test Scheduled Notification

```bash
curl -X POST http://localhost:8089/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": 1,
    "recipientEmail": "test@example.com",
    "recipientName": "Test User",
    "notificationType": "APPOINTMENT_REMINDER",
    "channel": "EMAIL",
    "subject": "Scheduled Test",
    "message": "This notification will be sent at the scheduled time",
    "scheduledTime": "2026-01-20T10:00:00"
  }'
```

## Notification Types

- `APPOINTMENT_REMINDER` - Appointment reminders
- `APPOINTMENT_CONFIRMATION` - Appointment confirmations
- `APPOINTMENT_CANCELLATION` - Cancellation notices
- `TEST_RESULT_ALERT` - Test results available
- `PRESCRIPTION_REMINDER` - Medication reminders
- `PRESCRIPTION_READY` - Prescription ready for pickup
- `BILLING_NOTIFICATION` - Billing updates
- `GENERAL_NOTIFICATION` - General purpose notifications
- `EMERGENCY_ALERT` - Emergency alerts

## Notification Channels

- `EMAIL` - Email only
- `SMS` - SMS only
- `BOTH` - Both Email and SMS

## Troubleshooting

### Email Not Sending

1. Check Gmail App Password is correct
2. Verify 2FA is enabled on Gmail
3. Check firewall allows SMTP port 587
4. Review logs: `tail -f logs/notification-service.log`

### SMS Not Sending

1. Verify Twilio credentials
2. Check phone number format (E.164: +1234567890)
3. Verify Twilio account has credits
4. Check Twilio verified numbers list

### Kafka Connection Issues

1. Verify Kafka is running: `docker ps | grep kafka`
2. Check bootstrap servers configuration
3. Verify topics exist: `docker exec kafka kafka-topics --list --bootstrap-server localhost:9092`

## Monitoring

Check service health:
```bash
curl http://localhost:8089/actuator/health
```

View notification statistics:
```bash
# Get all notifications for a recipient
curl http://localhost:8089/api/notifications/recipient/1

# Check specific notification status
curl http://localhost:8089/api/notifications/123
```

## Integration with Other Services

### From Appointment Service

```java
AppointmentEventDTO event = AppointmentEventDTO.builder()
    .patientEmail("patient@example.com")
    .patientName("John Doe")
    .doctorName("Dr. Smith")
    .appointmentTime(LocalDateTime.now().plusDays(1))
    .eventType("CREATED")
    .build();

kafkaTemplate.send("appointment-events", event);
```

### From Prescription Service

```java
PrescriptionEventDTO event = PrescriptionEventDTO.builder()
    .patientEmail("patient@example.com")
    .medicationName("Amoxicillin")
    .dosage("500mg")
    .eventType("CREATED")
    .build();

kafkaTemplate.send("prescription-events", event);
```

## License

© 2026 Healthcare System