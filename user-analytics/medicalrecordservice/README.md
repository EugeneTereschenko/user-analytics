# Medical Record Service Setup Guide

## Overview

The Medical Record Service manages comprehensive electronic health records (EHR) including diagnoses, prescriptions, lab results, vital signs, and medical attachments.

## Features

✅ **Complete EHR** - Comprehensive medical record management  
✅ **Prescriptions** - Medication tracking and management  
✅ **Lab Results** - Laboratory test results storage  
✅ **Attachments** - Medical images and documents  
✅ **Vital Signs** - Blood pressure, heart rate, temperature tracking  
✅ **Record Lifecycle** - Draft → Finalized → Signed → Archived  
✅ **Confidentiality** - Support for confidential records  
✅ **Search & Filter** - By patient, doctor, type, date range

## Database Schema

### Main Tables

1. **medical_records** - Core medical records
2. **prescriptions** - Medication prescriptions
3. **lab_results** - Laboratory test results
4. **attachments** - Medical images/documents
5. **vital_signs** - Vital sign measurements

## Setup Instructions

### Step 1: Create Directory Structure

```bash
cd ~/IdeaProjects/demo/user-analytics

# Create medicalrecordservice module
mkdir -p medicalrecordservice/src/main/java/com/healthcare/medicalrecordservice/{controller,service,repository,entity,dto,mapper,exception}
mkdir -p medicalrecordservice/src/main/resources/db/changelog
mkdir -p medicalrecordservice/uploads/medical-records
```

### Step 2: Create Database

```bash
psql -U postgres -c "CREATE DATABASE medical_record_db;"
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
    <module>medicalrecordservice</module>  <!-- Add this -->
</modules>
```

### Step 5: Create Liquibase Files

**File:** `src/main/resources/db/changelog/db.changelog-master.yaml`

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changelog-v1.0-medical-record-tables.yaml
```

**File:** `src/main/resources/db/changelog/changelog-v1.0-medical-record-tables.yaml`

Copy the content from the "Medical Record Liquibase Changelog" artifact above.

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
```

### Step 7: Verify

**Eureka Dashboard:** http://localhost:8761

You should see all services registered:
- PATIENT-SERVICE
- APPOINTMENT-SERVICE
- MEDICAL-RECORD-SERVICE

**Health Check:** http://localhost:8084/medical-record-service/actuator/health

## API Endpoints

### Base URL
```
http://localhost:8083/medical-record-service/api/v1/medical-records
```

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create medical record |
| GET | `/{id}` | Get record by ID |
| GET | `/` | Get all records (paginated) |
| GET | `/patient/{patientId}` | Get patient's records |
| GET | `/doctor/{doctorId}` | Get doctor's records |
| GET | `/type/{recordType}` | Get records by type |
| GET | `/status/{status}` | Get records by status |
| GET | `/appointment/{appointmentId}` | Get records for appointment |
| GET | `/patient/{patientId}/between?startDate=&endDate=` | Get patient records in range |
| GET | `/patient/{patientId}/non-confidential` | Get non-confidential records |
| GET | `/patient/{patientId}/count` | Count patient records |
| GET | `/search?query=` | Search records |
| PUT | `/{id}` | Update record |
| PATCH | `/{id}/finalize` | Finalize record |
| PATCH | `/{id}/sign?signedBy=` | Sign record |
| PATCH | `/{id}/archive` | Archive record |
| DELETE | `/{id}` | Delete record |

### Record Types

- `CONSULTATION` - General consultation
- `DIAGNOSIS` - Medical diagnosis
- `PRESCRIPTION` - Prescription record
- `LAB_RESULT` - Laboratory results
- `IMAGING` - X-ray, MRI, CT scan
- `SURGERY` - Surgical procedure
- `VACCINATION` - Vaccination record
- `HOSPITAL_ADMISSION` - Hospital admission
- `DISCHARGE_SUMMARY` - Discharge summary
- `PROGRESS_NOTE` - Progress notes
- `PROCEDURE` - Medical procedure
- `REFERRAL` - Referral to specialist

### Record Status

- `DRAFT` - Initial state
- `FINALIZED` - Completed but not signed
- `SIGNED` - Signed by doctor
- `AMENDED` - Modified after signing
- `ARCHIVED` - Archived record

## Testing

### Test 1: Create Medical Record

```bash
curl -X POST http://localhost:8084/medical-record-service/api/v1/medical-records \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentId": 1,
    "recordDate": "2025-12-25",
    "recordType": "CONSULTATION",
    "title": "Annual Checkup",
    "diagnosis": "Patient in good health",
    "symptoms": "None reported",
    "treatment": "Continue current lifestyle",
    "notes": "Patient advised to maintain healthy diet",
    "patientName": "John Doe",
    "doctorName": "Dr. Smith",
    "isConfidential": false
  }'
```

### Test 2: Create Record with Vital Signs

```bash
curl -X POST http://localhost:8084/medical-record-service/api/v1/medical-records \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "recordDate": "2025-12-25",
    "recordType": "CONSULTATION",
    "title": "Regular Checkup",
    "diagnosis": "Hypertension",
    "vitalSigns": [
      {
        "vitalType": "Blood Pressure",
        "value": "140/90",
        "unit": "mmHg",
        "recordedAt": "2025-12-25T10:00:00"
      },
      {
        "vitalType": "Heart Rate",
        "value": "75",
        "unit": "bpm",
        "recordedAt": "2025-12-25T10:00:00"
      },
      {
        "vitalType": "Temperature",
        "value": "37.0",
        "unit": "°C",
        "recordedAt": "2025-12-25T10:00:00"
      }
    ]
  }'
```

### Test 3: Get Patient Records

```bash
curl http://localhost:8084/medical-record-service/api/v1/medical-records/patient/1
```

### Test 4: Get Records Between Dates

```bash
curl "http://localhost:8084/medical-record-service/api/v1/medical-records/patient/1/between?startDate=2025-01-01&endDate=2025-12-31"
```

### Test 5: Search Records

```bash
curl "http://localhost:8084/medical-record-service/api/v1/medical-records/search?query=hypertension"
```

### Test 6: Finalize Record

```bash
curl -X PATCH http://localhost:8084/medical-record-service/api/v1/medical-records/1/finalize
```

### Test 7: Sign Record

```bash
curl -X PATCH "http://localhost:8084/medical-record-service/api/v1/medical-records/1/sign?signedBy=Dr.+Smith"
```

### Test 8: Get Non-Confidential Records

```bash
curl http://localhost:8084/medical-record-service/api/v1/medical-records/patient/1/non-confidential
```

### Test 9: Get Record Count

```bash
curl http://localhost:8084/medical-record-service/api/v1/medical-records/patient/1/count
```

## Database Schema Details

### medical_records
```
├── id
├── patient_id (indexed)
├── doctor_id (indexed)
├── appointment_id (indexed)
├── record_date (indexed)
├── record_type
├── title
├── diagnosis (searchable)
├── symptoms (searchable)
├── treatment
├── notes
├── patient_name
├── doctor_name
├── is_confidential
├── status (indexed)
├── created_at
├── updated_at
├── signed_at
└── signed_by
```

### prescriptions
```
├── id
├── medical_record_id (FK, indexed)
├── medication_name
├── dosage
├── frequency
├── duration
├── quantity
├── refills
├── instructions
├── start_date
├── end_date
├── status
└── prescribed_at
```

### lab_results
```
├── id
├── medical_record_id (FK, indexed)
├── test_name
├── test_code
├── result_value
├── unit
├── reference_range
├── is_abnormal
├── interpretation
├── test_date
├── result_date
├── laboratory_name
├── performed_by
├── status
└── created_at
```

### attachments
```
├── id
├── medical_record_id (FK, indexed)
├── file_name
├── file_path
├── file_type
├── file_size
├── attachment_type
├── description
├── uploaded_by
└── uploaded_at
```

### vital_signs
```
├── medical_record_id (FK, indexed)
├── vital_type
├── value
├── unit
└── recorded_at
```

## Project Structure

```
medicalrecordservice/
├── src/main/java/com/healthcare/medicalrecordservice/
│   ├── controller/
│   │   └── MedicalRecordController.java
│   ├── service/
│   │   └── MedicalRecordService.java
│   ├── repository/
│   │   └── MedicalRecordRepository.java
│   ├── entity/
│   │   ├── MedicalRecord.java
│   │   ├── Prescription.java
│   │   ├── LabResult.java
│   │   └── Attachment.java
│   ├── dto/
│   │   ├── MedicalRecordDTO.java
│   │   ├── PrescriptionDTO.java
│   │   ├── LabResultDTO.java
│   │   └── AttachmentDTO.java
│   ├── mapper/
│   │   └── MedicalRecordMapper.java
│   ├── exception/
│   │   ├── MedicalRecordNotFoundException.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ErrorResponse.java
│   └── MedicalRecordServiceApplication.java
└── src/main/resources/
    ├── application.yml
    └── db/changelog/
        ├── db.changelog-master.yaml
        └── changelog-v1.0-medical-record-tables.yaml
```

## Integration Points

### With Patient Service
- Store patient_id reference
- Can fetch full patient details

### With Appointment Service
- Link records to appointments
- Track consultation outcomes

### With Doctor Service (Future)
- Link to doctor profiles
- Track doctor's workload

## Security Considerations

🔐 **HIPAA Compliance** - Implement proper access controls  
🛡️ **Confidential Records** - Restrict access to authorized personnel  
🔒 **Encryption** - Encrypt sensitive data at rest and in transit  
📝 **Audit Logs** - Track all access to medical records  
⏰ **Retention Policies** - Implement data retention rules

## Future Enhancements

1. **File Upload API** - Upload X-rays, documents
2. **Document Generation** - Generate PDF reports
3. **Encryption** - Encrypt sensitive fields
4. **Audit Trail** - Track who accessed what when
5. **Integration** - Connect with lab systems, imaging systems
6. **FHIR Support** - Implement FHIR standards
7. **Telemedicine Integration** - Link with video consultation records

## Troubleshooting

### Port Already in Use
```bash
lsof -i :8084
kill -9 <PID>
```

### Database Connection Error
```bash
psql -U postgres -c "CREATE DATABASE medical_record_db;"
```

### Upload Directory Not Found
```bash
mkdir -p ~/IdeaProjects/demo/user-analytics/medicalrecordservice/uploads/medical-records
```

## All Services Running

| Service | Port | URL                   |
|---------|------|-----------------------|
| Eureka Server | 8761 | http://localhost:8761 |
| Patient Service | 8081 | http://localhost:8081 |
| Appointment Service | 8083 | http://localhost:8083 |
| Medical Record Service | 8083 | http://localhost:8084 |

Your healthcare microservices architecture is complete! 🎉