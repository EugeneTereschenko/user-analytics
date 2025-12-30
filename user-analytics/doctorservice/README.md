# Doctor/Staff Service Setup Guide

## Overview

The Doctor/Staff Service manages healthcare providers (doctors) and staff members, including their schedules, qualifications, and availability.

## Features

✅ **Doctor Management** - Complete CRUD for doctors  
✅ **Staff Management** - Manage nurses, technicians, admin staff  
✅ **Schedules** - Doctor availability and working hours  
✅ **Specializations** - Track medical specializations  
✅ **Qualifications** - Store degrees and certifications  
✅ **Multi-language Support** - Track languages spoken  
✅ **Status Management** - Active, on leave, inactive, retired  
✅ **Search & Filter** - By specialization, department, role, status

## Database Schema

### Main Tables

1. **doctors** - Doctor profiles and information
2. **staff** - Staff member information
3. **schedules** - Doctor working schedules
4. **addresses** - Address information
5. **doctor_qualifications** - Doctor qualifications
6. **doctor_languages** - Languages spoken by doctors

## Setup Instructions

### Step 1: Create Directory Structure

```bash
cd ~/IdeaProjects/demo/user-analytics

# Create doctorservice module
mkdir -p doctorservice/src/main/java/com/healthcare/doctorservice/{controller,service,repository,entity,dto,mapper,exception}
mkdir -p doctorservice/src/main/resources/db/changelog
```

### Step 2: Create Database

```bash
psql -U postgres -c "CREATE DATABASE doctor_db;"
```

### Step 3: Copy All Files

Copy content from artifacts above to create these files in your project.

### Step 4: Update Parent POM

Add to modules in parent pom.xml:

```xml
<modules>
    <module>demo</module>
    <module>patientservice</module>
    <module>eureka-server</module>
    <module>appointmentservice</module>
    <module>medicalrecordservice</module>
    <module>doctorservice</module>  <!-- Add this -->
</modules>
```

### Step 5: Create Liquibase Files

**File:** `src/main/resources/db/changelog/db.changelog-master.yaml`

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changelog-v1.0-doctor-tables.yaml
```

**File:** `src/main/resources/db/changelog/changelog-v1.0-doctor-tables.yaml`

Copy the content from the "Doctor Service Liquibase Changelog" artifact above.

### Step 6: Build and Run

```bash
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

# Terminal 4 - Medical Record Service
cd ../medicalrecordservice
mvn spring-boot:run

# Terminal 5 - Doctor Service
cd ../doctorservice
mvn spring-boot:run
```

### Step 7: Verify

**Eureka Dashboard:** http://localhost:8761

You should see all services registered:
- PATIENT-SERVICE
- APPOINTMENT-SERVICE
- MEDICAL-RECORD-SERVICE
- DOCTOR-SERVICE

**Health Check:** http://localhost:8085/doctor-service/actuator/health

## API Endpoints

### Doctor Endpoints

Base URL: `http://localhost:8085/doctor-service/api/v1/doctors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create doctor |
| GET | `/{id}` | Get doctor by ID |
| GET | `/email/{email}` | Get doctor by email |
| GET | `/license/{licenseNumber}` | Get doctor by license |
| GET | `/` | Get all doctors (paginated) |
| GET | `/specialization/{specialization}` | Get by specialization |
| GET | `/status/{status}` | Get by status |
| GET | `/department/{department}` | Get by department |
| GET | `/active` | Get all active doctors |
| GET | `/search?query=` | Search doctors |
| GET | `/specializations` | Get all specializations |
| GET | `/departments` | Get all departments |
| PUT | `/{id}` | Update doctor |
| PATCH | `/{id}/status?status=` | Update status |
| DELETE | `/{id}` | Delete doctor |

### Staff Endpoints

Base URL: `http://localhost:8085/doctor-service/api/v1/staff`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create staff |
| GET | `/{id}` | Get staff by ID |
| GET | `/email/{email}` | Get staff by email |
| GET | `/` | Get all staff (paginated) |
| GET | `/role/{role}` | Get by role |
| GET | `/status/{status}` | Get by status |
| GET | `/department/{department}` | Get by department |
| GET | `/active` | Get all active staff |
| GET | `/search?query=` | Search staff |
| PUT | `/{id}` | Update staff |
| PATCH | `/{id}/status?status=` | Update status |
| DELETE | `/{id}` | Delete staff |

## Enums and Values

### Doctor Status
- `ACTIVE` - Currently working
- `ON_LEAVE` - Temporarily unavailable
- `INACTIVE` - Not currently practicing
- `RETIRED` - Retired from practice
- `SUSPENDED` - Temporarily suspended

### Staff Roles
- `NURSE` - Registered nurse
- `RECEPTIONIST` - Front desk staff
- `PHARMACIST` - Pharmacy staff
- `LAB_TECHNICIAN` - Laboratory technician
- `RADIOLOGIST` - Radiology technician
- `ADMINISTRATOR` - Administrative staff
- `ACCOUNTANT` - Accounting staff
- `IT_SUPPORT` - IT support
- `SECURITY` - Security personnel
- `JANITOR` - Janitorial staff
- `OTHER` - Other roles

### Staff Status
- `ACTIVE` - Currently working
- `ON_LEAVE` - On leave
- `INACTIVE` - Not currently working
- `TERMINATED` - Employment terminated
- `RESIGNED` - Resigned from position

## Testing

### Test 1: Create Doctor

```bash
curl -X POST http://localhost:8085/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "email": "dr.smith@hospital.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1980-05-15",
    "gender": "MALE",
    "licenseNumber": "MD123456",
    "specialization": "Cardiology",
    "qualifications": ["MD", "MBBS", "Fellowship in Cardiology"],
    "languages": ["English", "Spanish"],
    "yearsOfExperience": 15,
    "consultationFee": 150.00,
    "biography": "Experienced cardiologist with 15 years of practice",
    "department": "Cardiology",
    "roomNumber": "301",
    "joinedDate": "2020-01-15"
  }'
```

### Test 2: Create Doctor with Schedule

```bash
curl -X POST http://localhost:8085/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Sarah",
    "lastName": "Johnson",
    "email": "dr.johnson@hospital.com",
    "phoneNumber": "+1234567891",
    "gender": "FEMALE",
    "specialization": "Pediatrics",
    "consultationFee": 100.00,
    "department": "Pediatrics",
    "schedules": [
      {
        "dayOfWeek": "MONDAY",
        "startTime": "09:00:00",
        "endTime": "17:00:00",
        "slotDurationMinutes": 30
      },
      {
        "dayOfWeek": "WEDNESDAY",
        "startTime": "09:00:00",
        "endTime": "17:00:00",
        "slotDurationMinutes": 30
      }
    ]
  }'
```

### Test 3: Create Staff (Nurse)

```bash
curl -X POST http://localhost:8085/doctor-service/api/v1/staff \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Mary",
    "lastName": "Williams",
    "email": "mary.williams@hospital.com",
    "phoneNumber": "+1234567892",
    "dateOfBirth": "1985-08-20",
    "gender": "FEMALE",
    "employeeId": "EMP001",
    "role": "NURSE",
    "department": "Emergency",
    "shift": "Morning",
    "salary": 65000.00,
    "joinedDate": "2018-03-01"
  }'
```

### Test 4: Get All Active Doctors

```bash
curl http://localhost:8085/doctor-service/api/v1/doctors/active
```

### Test 5: Search Doctors by Specialization

```bash
curl http://localhost:8085/doctor-service/api/v1/doctors/specialization/Cardiology
```

### Test 6: Get All Specializations

```bash
curl http://localhost:8085/doctor-service/api/v1/doctors/specializations
```

### Test 7: Search Staff by Role

```bash
curl http://localhost:8085/doctor-service/api/v1/staff/role/NURSE
```

### Test 8: Update Doctor Status

```bash
curl -X PATCH "http://localhost:8085/doctor-service/api/v1/doctors/1/status?status=ON_LEAVE"
```

### Test 9: Search Doctors

```bash
curl "http://localhost:8085/doctor-service/api/v1/doctors/search?query=cardio"
```

## Database Schema Details

### doctors
```
├── id
├── first_name
├── last_name
├── email (unique, indexed)
├── phone_number
├── date_of_birth
├── gender
├── license_number (unique)
├── specialization (indexed)
├── years_of_experience
├── consultation_fee
├── biography
├── address_id (FK)
├── status (indexed)
├── joined_date
├── profile_image_url
├── department (indexed)
├── room_number
├── emergency_contact_name
├── emergency_contact_phone
├── created_at
└── updated_at
```

### schedules
```
├── id
├── doctor_id (FK, indexed)
├── day_of_week
├── start_time
├── end_time
├── slot_duration_minutes
├── is_available
└── notes
```

### staff
```
├── id
├── first_name
├── last_name
├── email (unique, indexed)
├── phone_number
├── date_of_birth
├── gender
├── employee_id (unique)
├── role (indexed)
├── department (indexed)
├── shift
├── salary
├── joined_date
├── address_id (FK)
├── status (indexed)
├── supervisor_id
├── emergency_contact_name
├── emergency_contact_phone
├── profile_image_url
├── created_at
└── updated_at
```

## Integration with Other Services

### With Appointment Service
- Provides doctor availability for scheduling
- Validates doctor IDs in appointments

### With Medical Record Service
- Links medical records to doctors
- Tracks doctor's patient interactions

### With Patient Service
- Display doctor information to patients
- Show doctor specializations for selection

## Common Specializations

- Cardiology
- Pediatrics
- Orthopedics
- Dermatology
- Neurology
- Oncology
- Radiology
- Psychiatry
- General Practice
- Internal Medicine
- Surgery
- Obstetrics & Gynecology

## Future Enhancements

1. **Doctor Ratings** - Patient reviews and ratings
2. **Availability Calendar** - Real-time availability
3. **Certifications** - Track medical certifications and expiry
4. **Performance Metrics** - Track consultation stats
5. **Leave Management** - Doctor leave requests and approval
6. **Shift Management** - Staff shift scheduling
7. **Credentials Verification** - Automated license verification

## Troubleshooting

### Port Already in Use
```bash
lsof -i :8085
kill -9 <PID>
```

### Database Connection Error
```bash
psql -U postgres -c "CREATE DATABASE doctor_db;"
```

## Complete Microservices Architecture

| Service | Port | URL                   |
|---------|------|-----------------------|
| Eureka Server | 8761 | http://localhost:8761 |
| Patient Service | 8081 | http://localhost:8081 |
| Appointment Service | 8083 | http://localhost:8083 |
| Medical Record Service | 8084 | http://localhost:8084 |
| Doctor/Staff Service | 8085 | http://localhost:8085 |

Your complete healthcare microservices ecosystem is now ready! 🎉🏥