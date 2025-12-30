# Appointment Service Setup Guide

## Overview

The Appointment Service manages all appointment-related operations including scheduling, rescheduling, cancellation, and confirmation.

## Features

✅ **CRUD Operations** - Create, read, update, delete appointments  
✅ **Smart Scheduling** - Conflict detection for doctor availability  
✅ **Status Management** - Track appointment lifecycle  
✅ **Time Validation** - Business hours and weekend restrictions  
✅ **Flexible Queries** - Search by patient, doctor, date range, status  
✅ **Service Discovery** - Integrates with Eureka  
✅ **Database Migration** - Liquibase for schema management

## Project Structure

```
appointmentservice/
├── src/
│   └── main/
│       ├── java/com/healthcare/appointmentservice/
│       │   ├── controller/
│       │   │   └── AppointmentController.java
│       │   ├── service/
│       │   │   └── AppointmentService.java
│       │   ├── repository/
│       │   │   └── AppointmentRepository.java
│       │   ├── entity/
│       │   │   └── Appointment.java
│       │   ├── dto/
│       │   │   └── AppointmentDTO.java
│       │   ├── mapper/
│       │   │   └── AppointmentMapper.java
│       │   ├── exception/
│       │   │   ├── AppointmentNotFoundException.java
│       │   │   ├── AppointmentConflictException.java
│       │   │   ├── InvalidAppointmentException.java
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── ErrorResponse.java
│       │   └── AppointmentServiceApplication.java
│       └── resources/
│           ├── application.yml
│           └── db/
│               └── changelog/
│                   ├── db.changelog-master.yaml
│                   └── changelog-v1.0-appointment-tables.yaml
└── pom.xml
```

## Setup Instructions

### Step 1: Create Directory Structure

```bash
cd ~/IdeaProjects/demo/user-analytics

# Create appointmentservice module
mkdir -p appointmentservice/src/main/java/com/healthcare/appointmentservice/{controller,service,repository,entity,dto,mapper,exception}
mkdir -p appointmentservice/src/main/resources/db/changelog
```

### Step 2: Create Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE appointment_db;

# Exit
\q
```

### Step 3: Copy All Files

Copy the content from the artifacts above to create these files:

1. **pom.xml** - Maven dependencies
2. **application.yml** - Configuration
3. **AppointmentServiceApplication.java** - Main application
4. **Appointment.java** - Entity
5. **AppointmentDTO.java** - Data Transfer Object
6. **AppointmentRepository.java** - Database operations
7. **AppointmentMapper.java** - DTO/Entity mapping
8. **AppointmentService.java** - Business logic
9. **AppointmentController.java** - REST endpoints
10. **Exception classes** - Error handling
11. **Liquibase changelogs** - Database schema

### Step 4: Update Parent POM

Add `appointmentservice` to the parent POM modules:

```xml
<modules>
    <module>demo</module>
    <module>patientservice</module>
    <module>eureka-server</module>
    <module>appointmentservice</module>  <!-- Add this -->
</modules>
```

### Step 5: Build and Run

```bash
# Build all services
cd ~/IdeaProjects/demo/user-analytics
mvn clean install

# Terminal 1 - Eureka Server
cd eureka-server
mvn spring-boot:run

# Terminal 2 - Patient Service
cd ../patientservice
mvn spring-boot:run

# Terminal 3 - Appointment Service
cd ../appointmentservice
mvn spring-boot:run
```

### Step 6: Verify

**Eureka Dashboard:** http://localhost:8761  
You should see:
- PATIENT-SERVICE
- APPOINTMENT-SERVICE

**Appointment Service Health:** http://localhost:8083/appointment-service/actuator/health

## API Endpoints

### Base URL
```
http://localhost:8082/appointment-service/api/v1/appointments
```

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create appointment |
| GET | `/{id}` | Get appointment by ID |
| GET | `/` | Get all appointments (paginated) |
| GET | `/patient/{patientId}` | Get patient's appointments |
| GET | `/doctor/{doctorId}` | Get doctor's appointments |
| GET | `/status/{status}` | Get appointments by status |
| GET | `/between?startDate=&endDate=` | Get appointments in date range |
| GET | `/today` | Get today's appointments |
| PUT | `/{id}` | Update appointment |
| PATCH | `/{id}/reschedule?newDateTime=` | Reschedule appointment |
| PATCH | `/{id}/cancel?reason=` | Cancel appointment |
| PATCH | `/{id}/confirm` | Confirm appointment |
| PATCH | `/{id}/complete?notes=` | Complete appointment |
| DELETE | `/{id}` | Delete appointment |

### Appointment Statuses

- `SCHEDULED` - Initial state
- `CONFIRMED` - Patient confirmed
- `IN_PROGRESS` - Currently happening
- `COMPLETED` - Finished successfully
- `CANCELLED` - Cancelled by patient or doctor
- `NO_SHOW` - Patient didn't show up
- `RESCHEDULED` - Moved to new time

### Appointment Types

- `CONSULTATION` - General consultation
- `FOLLOW_UP` - Follow-up visit
- `EMERGENCY` - Emergency appointment
- `ROUTINE_CHECKUP` - Regular checkup
- `VACCINATION` - Vaccination appointment
- `LABORATORY` - Lab tests
- `IMAGING` - X-ray, MRI, etc.
- `SURGERY` - Surgical procedure
- `THERAPY` - Physical/occupational therapy

## Testing

### Test 1: Create Appointment

```bash
curl -X POST http://localhost:8083/appointment-service/api/v1/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDateTime": "2025-12-27T10:00:00",
    "durationMinutes": 30,
    "appointmentType": "CONSULTATION",
    "reason": "Regular checkup",
    "patientName": "John Doe",
    "patientEmail": "john.doe@example.com",
    "patientPhone": "+1234567890",
    "doctorName": "Dr. Smith",
    "doctorSpecialization": "Cardiology"
  }'
```

### Test 2: Get Patient's Appointments

```bash
curl http://localhost:8083/appointment-service/api/v1/appointments/patient/1
```

### Test 3: Get Doctor's Appointments

```bash
curl http://localhost:8083/appointment-service/api/v1/appointments/doctor/1
```

### Test 4: Get Today's Appointments

```bash
curl http://localhost:8083/appointment-service/api/v1/appointments/today
```

### Test 5: Reschedule Appointment

```bash
curl -X PATCH "http://localhost:8083/appointment-service/api/v1/appointments/1/reschedule?newDateTime=2025-12-28T14:00:00"
```

### Test 6: Cancel Appointment

```bash
curl -X PATCH "http://localhost:8082/appointment-service/api/v1/appointments/1/cancel?reason=Patient+illness"
```

### Test 7: Confirm Appointment

```bash
curl -X PATCH http://localhost:8083/appointment-service/api/v1/appointments/1/confirm
```

### Test 8: Complete Appointment

```bash
curl -X PATCH "http://localhost:8083/appointment-service/api/v1/appointments/1/complete?notes=Patient+doing+well"
```

### Test 9: Get Appointments Between Dates

```bash
curl "http://localhost:8083/appointment-service/api/v1/appointments/between?startDate=2025-12-27T00:00:00&endDate=2025-12-31T23:59:59"
```

## Business Rules

### Scheduling Restrictions

- ⏰ **Business Hours:** 8:00 AM - 6:00 PM only
- 📅 **Weekdays Only:** No weekend appointments
- 🚫 **No Past Dates:** Cannot schedule in the past
- ⚠️ **Conflict Detection:** Doctor can't have overlapping appointments

### Duration Constraints

- **Minimum:** 15 minutes
- **Maximum:** 8 hours (480 minutes)
- **Default:** 30 minutes

## Database Schema

```sql
appointments
├── id (Primary Key)
├── patient_id (Foreign Key reference)
├── doctor_id (Foreign Key reference)
├── appointment_date_time
├── duration_minutes
├── status
├── appointment_type
├── reason
├── notes
├── patient_name (denormalized for quick access)
├── patient_email
├── patient_phone
├── doctor_name (denormalized for quick access)
├── doctor_specialization
├── created_at
├── updated_at
├── cancelled_at
└── cancellation_reason

Indexes:
- idx_appointments_patient_id
- idx_appointments_doctor_id
- idx_appointments_status
- idx_appointments_date_time
- idx_appointments_doctor_date (composite)
```

## Integration with Other Services

### Future Enhancements

1. **Patient Service Integration**
    - Fetch patient details via REST/Feign
    - Validate patient exists

2. **Doctor Service Integration**
    - Fetch doctor details and availability
    - Check doctor schedule

3. **Notification Service**
    - Send appointment confirmations
    - Send reminders 24 hours before
    - Send cancellation notifications

4. **Event-Driven Architecture**
    - Publish events when appointments are created/cancelled
    - Allow other services to react to appointment changes

## Troubleshooting

### Port Already in Use

```bash
# Check what's using port 8082
lsof -i :8083

# Kill the process
kill -9 <PID>
```

### Database Connection Error

```bash
# Verify database exists
psql -U postgres -l | grep appointment_db

# If not, create it
psql -U postgres -c "CREATE DATABASE appointment_db;"
```

### Eureka Connection Refused

Make sure Eureka Server is running on port 8761 before starting Appointment Service.

## Performance Tips

1. **Use Pagination** - Always use pageable for large result sets
2. **Index Usage** - Queries on patient_id, doctor_id, and date are optimized
3. **Caching** - Consider adding Redis for frequently accessed data
4. **Connection Pooling** - Configure HikariCP for optimal database connections

## Security Considerations

- 🔐 Add authentication/authorization (JWT)
- 🛡️ Validate user can only access their own appointments
- 🔒 Encrypt sensitive patient information
- 📝 Audit log all appointment operations
- ⚡ Rate limiting for API endpoints

## Next Steps

1. **Add Doctor Service** - Manage doctor profiles and specializations
2. **Add Notification Service** - Email/SMS notifications
3. **Add API Gateway** - Centralized routing and authentication
4. **Add Caching** - Redis for performance
5. **Add Tests** - Unit and integration tests

Would you like me to help implement any of these next?