# Prescription Service Setup Guide

## Overview

The Prescription Service manages electronic prescriptions (e-prescriptions) including medication details, refills, dispensing, and prescription lifecycle management.

## Features

✅ **E-Prescriptions** - Digital prescription management  
✅ **Multiple Medications** - Support multiple medications per prescription  
✅ **Refill Management** - Track refills allowed and remaining  
✅ **Dispensing Tracking** - Record when prescriptions are filled  
✅ **Expiration Management** - Automatic expiration detection  
✅ **Status Lifecycle** - Active → Dispensed → Completed/Expired  
✅ **Controlled Substances** - Flag controlled substances  
✅ **Generic Substitution** - Support for generic alternatives  
✅ **Administration Routes** - Oral, IV, topical, etc.

## Setup Instructions

### Step 1: Create Directory Structure

```bash
cd ~/IdeaProjects/demo/user-analytics

# Create prescriptionservice module
mkdir -p prescriptionservice/src/main/java/com/healthcare/prescriptionservice/{controller,service,repository,entity,dto,mapper,exception}
mkdir -p prescriptionservice/src/main/resources/db/changelog
```

### Step 2: Create Database

```bash
psql -U postgres -c "CREATE DATABASE prescription_db;"
```

### Step 3: Update Parent POM

Add to modules:

```xml
<modules>
    <module>demo</module>
    <module>patientservice</module>
    <module>eureka-server</module>
    <module>appointmentservice</module>
    <module>medicalrecordservice</module>
    <module>doctorservice</module>
    <module>billingservice</module>
    <module>prescriptionservice</module>  <!-- Add this -->
</modules>
```

### Step 4: Create Liquibase Files

**File:** `src/main/resources/db/changelog/db.changelog-master.yaml`

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changelog-v1.0-prescription-tables.yaml
```

### Step 5: Build and Run

```bash
cd ~/IdeaProjects/demo/user-analytics
mvn clean install

cd prescriptionservice
mvn spring-boot:run
```

### Step 6: Verify

**Eureka:** http://localhost:8761  
**Health:** http://localhost:8086/prescription-service/actuator/health

## API Endpoints

### Base URL
```
http://localhost:8086/prescription-service/api/v1/prescriptions
```

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create prescription |
| GET | `/{id}` | Get by ID |
| GET | `/number/{prescriptionNumber}` | Get by number |
| GET | `/` | Get all (paginated) |
| GET | `/patient/{patientId}` | Get patient prescriptions |
| GET | `/doctor/{doctorId}` | Get doctor prescriptions |
| GET | `/status/{status}` | Get by status |
| GET | `/patient/{patientId}/refillable` | Get refillable |
| GET | `/expired` | Get expired |
| GET | `/search?query=` | Search |
| PUT | `/{id}` | Update |
| PATCH | `/{id}/dispense?dispensedBy=` | Dispense |
| PATCH | `/{id}/refill` | Refill |
| PATCH | `/{id}/cancel?reason=` | Cancel |
| POST | `/mark-expired` | Mark expired |
| DELETE | `/{id}` | Delete |

## Testing

### Test 1: Create Prescription

```bash
curl -X POST http://localhost:8086/prescription-service/api/v1/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "prescriptionDate": "2025-12-31",
    "validUntil": "2026-06-30",
    "patientName": "John Doe",
    "patientDateOfBirth": "1990-05-15",
    "doctorName": "Dr. Smith",
    "doctorLicense": "MD123456",
    "diagnosis": "Hypertension",
    "isRefillable": true,
    "refillsAllowed": 3,
    "medications": [
      {
        "medicationName": "Lisinopril",
        "genericName": "Lisinopril",
        "dosage": "10mg",
        "dosageForm": "Tablet",
        "strength": "10mg",
        "frequency": "Once daily",
        "duration": "30 days",
        "quantity": 30,
        "unit": "tablets",
        "route": "ORAL",
        "instructions": "Take one tablet daily in the morning",
        "startDate": "2025-12-31",
        "endDate": "2026-01-30"
      }
    ]
  }'
```

### Test 2: Get Patient Prescriptions

```bash
curl http://localhost:8086/prescription-service/api/v1/prescriptions/patient/1
```

### Test 3: Dispense Prescription

```bash
curl -X PATCH "http://localhost:8086/prescription-service/api/v1/prescriptions/1/dispense?dispensedBy=Pharmacist+John"
```

### Test 4: Refill Prescription

```bash
curl -X PATCH http://localhost:8086/prescription-service/api/v1/prescriptions/1/refill
```

### Test 5: Get Refillable Prescriptions

```bash
curl http://localhost:8086/prescription-service/api/v1/prescriptions/patient/1/refillable
```

### Test 6: Cancel Prescription

```bash
curl -X PATCH "http://localhost:8086/prescription-service/api/v1/prescriptions/1/cancel?reason=Patient+allergic+reaction"
```

## Database Schema

### prescriptions
```
├── id
├── prescription_number (Unique)
├── patient_id (indexed)
├── doctor_id (indexed)
├── medical_record_id
├── appointment_id
├── prescription_date (indexed)
├── valid_until
├── status (indexed)
├── patient_name
├── patient_date_of_birth
├── doctor_name
├── doctor_license
├── pharmacy_name
├── pharmacy_address
├── diagnosis
├── notes
├── is_refillable
├── refills_allowed
├── refills_remaining
├── dispensed_at
├── dispensed_by
├── created_at
├── updated_at
├── cancelled_at
└── cancellation_reason
```

### medications
```
├── id
├── prescription_id (FK, indexed)
├── medication_name
├── generic_name
├── drug_code
├── dosage
├── dosage_form
├── strength
├── frequency
├── duration
├── quantity
├── unit
├── route
├── instructions
├── start_date
├── end_date
├── warnings
├── side_effects
├── is_generic_allowed
├── is_controlled_substance
└── priority
```

## Enums

### Prescription Status
- `ACTIVE` - Currently active
- `DISPENSED` - Dispensed to patient
- `COMPLETED` - All refills used
- `CANCELLED` - Cancelled by doctor
- `EXPIRED` - Past valid until date
- `ON_HOLD` - Temporarily on hold

### Administration Routes
- `ORAL` - By mouth
- `TOPICAL` - Applied to skin
- `INTRAVENOUS` - IV injection
- `INTRAMUSCULAR` - IM injection
- `SUBCUTANEOUS` - Under skin
- `INHALATION` - Inhaled
- `RECTAL` - Rectal administration
- `OPHTHALMIC` - Eye drops
- `OTIC` - Ear drops
- `NASAL` - Nasal spray
- `TRANSDERMAL` - Through skin patch
- `OTHER` - Other routes

## Complete Healthcare System

| Service | Port | URL |
|---------|------|-----|
| Eureka Server | 8761 | http://localhost:8761 |
| Patient Service | 8081 | http://localhost:8081 |
| Appointment Service | 8083 | http://localhost:8083 |
| Medical Record Service | 8084 | http://localhost:8084 |
| Doctor/Staff Service | 8085 | http://localhost:8085 |
| Billing Service | 8082 | http://localhost:8082 |
| Prescription Service | 8086 | http://localhost:8086 |

Your complete healthcare microservices system is fully operational! 🎉💊