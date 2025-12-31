# Billing Service Setup Guide

## Overview

The Billing Service manages invoices, payments, and financial transactions for the healthcare system.

## Features

✅ **Invoice Management** - Create, track, and manage invoices  
✅ **Payment Processing** - Record and track payments  
✅ **Multiple Payment Methods** - Cash, card, insurance, etc.  
✅ **Invoice Status Tracking** - Draft, pending, paid, overdue  
✅ **Insurance Claims** - Track insurance claims and amounts  
✅ **Outstanding Balance** - Calculate and track balances  
✅ **Overdue Invoices** - Automatically identify overdue invoices  
✅ **Financial Reports** - Revenue tracking and analytics

## Database Schema

### Main Tables

1. **invoices** - Invoice headers
2. **invoice_items** - Line items on invoices
3. **payments** - Payment records

## Setup Instructions

### Step 1: Create Directory Structure

```bash
cd ~/IdeaProjects/demo/user-analytics

# Create billingservice module
mkdir -p billingservice/src/main/java/com/healthcare/billingservice/{controller,service,repository,entity,dto,mapper,exception}
mkdir -p billingservice/src/main/resources/db/changelog
```

### Step 2: Create Database

```bash
psql -U postgres -c "CREATE DATABASE billing_db;"
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
    <module>doctorservice</module>
    <module>billingservice</module>  <!-- Add this -->
</modules>
```

### Step 5: Create Liquibase Files

**File:** `src/main/resources/db/changelog/db.changelog-master.yaml`

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changelog-v1.0-billing-tables.yaml
```

**File:** `src/main/resources/db/changelog/changelog-v1.0-billing-tables.yaml`

Copy the content from the "Billing Service Liquibase Changelog" artifact above.

### Step 6: Build and Run

```bash
cd ~/IdeaProjects/demo/user-analytics
mvn clean install

# Run Billing Service
cd billingservice
mvn spring-boot:run
```

### Step 7: Verify

**Eureka Dashboard:** http://localhost:8761

You should see all services registered including:
- BILLING-SERVICE

**Health Check:** http://localhost:8082/billing-service/actuator/health

## API Endpoints

### Base URL
```
http://localhost:8085/billing-service/api/v1/billing
```

### Invoice Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/invoices` | Create invoice |
| GET | `/invoices/{id}` | Get invoice by ID |
| GET | `/invoices/number/{invoiceNumber}` | Get invoice by number |
| GET | `/invoices` | Get all invoices (paginated) |
| GET | `/invoices/patient/{patientId}` | Get patient invoices |
| GET | `/invoices/status/{status}` | Get invoices by status |
| GET | `/invoices/overdue` | Get overdue invoices |
| GET | `/invoices/patient/{patientId}/outstanding` | Get patient outstanding |
| GET | `/invoices/outstanding/total` | Get total outstanding |
| GET | `/invoices/search?query=` | Search invoices |
| PUT | `/invoices/{id}` | Update invoice |
| PATCH | `/invoices/{id}/send` | Send invoice |
| POST | `/invoices/{invoiceId}/payments` | Add payment |
| PATCH | `/invoices/{id}/cancel` | Cancel invoice |
| DELETE | `/invoices/{id}` | Delete invoice |

### Invoice Status

- `DRAFT` - Being created
- `PENDING` - Created but not sent
- `SENT` - Sent to patient
- `PARTIALLY_PAID` - Partially paid
- `PAID` - Fully paid
- `OVERDUE` - Past due date
- `CANCELLED` - Cancelled
- `REFUNDED` - Refunded

### Item Types

- `CONSULTATION` - Doctor consultation
- `PROCEDURE` - Medical procedure
- `MEDICATION` - Medication
- `LAB_TEST` - Laboratory test
- `IMAGING` - X-ray, MRI, CT scan
- `ROOM_CHARGE` - Hospital room
- `SURGERY` - Surgical procedure
- `EMERGENCY_SERVICE` - Emergency service
- `THERAPY` - Therapy session
- `VACCINATION` - Vaccination
- `EQUIPMENT` - Medical equipment
- `OTHER` - Other items

### Payment Methods

- `CASH` - Cash payment
- `CREDIT_CARD` - Credit card
- `DEBIT_CARD` - Debit card
- `BANK_TRANSFER` - Bank transfer
- `CHECK` - Check payment
- `INSURANCE` - Insurance payment
- `MOBILE_PAYMENT` - Mobile payment
- `ONLINE_PAYMENT` - Online payment
- `OTHER` - Other methods

## Testing

### Test 1: Create Invoice

```bash
curl -X POST http://localhost:8082/billing-service/api/v1/billing/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "appointmentId": 1,
    "doctorId": 1,
    "invoiceDate": "2025-12-31",
    "dueDate": "2026-01-15",
    "taxAmount": 10.00,
    "discountAmount": 0.00,
    "patientName": "John Doe",
    "patientEmail": "john.doe@example.com",
    "patientPhone": "+1234567890",
    "items": [
      {
        "itemType": "CONSULTATION",
        "description": "General consultation",
        "quantity": 1,
        "unitPrice": 100.00
      },
      {
        "itemType": "LAB_TEST",
        "description": "Blood test",
        "quantity": 1,
        "unitPrice": 50.00
      }
    ]
  }'
```

### Test 2: Get Patient Invoices

```bash
curl http://localhost:8082/billing-service/api/v1/billing/invoices/patient/1
```

### Test 3: Add Payment

```bash
curl -X POST http://localhost:8082/billing-service/api/v1/billing/invoices/1/payments \
  -H "Content-Type: application/json" \
  -d '{
    "paymentDate": "2025-12-31T10:00:00",
    "amount": 160.00,
    "paymentMethod": "CREDIT_CARD",
    "cardLastFour": "4242",
    "cardType": "Visa",
    "processedBy": "Admin"
  }'
```

### Test 4: Get Overdue Invoices

```bash
curl http://localhost:8082/billing-service/api/v1/billing/invoices/overdue
```

### Test 5: Get Total Outstanding

```bash
curl http://localhost:8082/billing-service/api/v1/billing/invoices/outstanding/total
```

### Test 6: Send Invoice

```bash
curl -X PATCH http://localhost:8082/billing-service/api/v1/billing/invoices/1/send
```

### Test 7: Search Invoices

```bash
curl "http://localhost:8082/billing-service/api/v1/billing/invoices/search?query=John"
```

### Test 8: Get Patient Outstanding Invoices

```bash
curl http://localhost:8082/billing-service/api/v1/billing/invoices/patient/1/outstanding
```

## Database Schema Details

### invoices
```
├── id (Primary Key)
├── invoice_number (Unique)
├── patient_id (indexed)
├── appointment_id
├── doctor_id
├── invoice_date (indexed)
├── due_date
├── subtotal
├── tax_amount
├── discount_amount
├── total_amount
├── paid_amount
├── balance_due
├── status (indexed)
├── patient_name
├── patient_email
├── patient_phone
├── insurance_provider
├── insurance_policy_number
├── insurance_claim_amount
├── notes
├── created_at
├── updated_at
├── sent_at
└── paid_at
```

### invoice_items
```
├── id (Primary Key)
├── invoice_id (FK, indexed)
├── item_type
├── description
├── quantity
├── unit_price
├── total_price
├── service_code
└── medical_record_id
```

### payments
```
├── id (Primary Key)
├── invoice_id (FK, indexed)
├── payment_reference (Unique)
├── payment_date (indexed)
├── amount
├── payment_method
├── status
├── transaction_id
├── card_last_four
├── card_type
├── receipt_number
├── notes
├── processed_by
└── created_at
```

## Business Logic

### Invoice Creation
1. Generate unique invoice number
2. Add invoice items
3. Calculate totals (subtotal, tax, discount)
4. Set initial status to DRAFT
5. Save invoice

### Payment Processing
1. Validate payment amount
2. Generate payment reference
3. Record payment
4. Update invoice paid_amount and balance_due
5. Update invoice status (PARTIALLY_PAID or PAID)

### Automatic Calculations
- **Subtotal** = Sum of all item total prices
- **Total Amount** = Subtotal + Tax - Discount
- **Balance Due** = Total Amount - Paid Amount

## Integration Points

### With Patient Service
- Link invoices to patients
- Fetch patient details

### With Appointment Service
- Link invoices to appointments
- Bill for appointments

### With Doctor Service
- Track doctor consultations
- Calculate consultation fees

### With Medical Record Service
- Link invoice items to medical records
- Bill for procedures and treatments

## Financial Reports

The service provides data for:
- Total revenue in date range
- Outstanding balance
- Overdue invoices count
- Payment methods breakdown
- Doctor revenue reports

## Future Enhancements

1. **PDF Generation** - Generate PDF invoices
2. **Email Integration** - Send invoices via email
3. **Payment Gateway** - Integrate with Stripe/PayPal
4. **Recurring Billing** - Subscription-based billing
5. **Financial Dashboard** - Revenue analytics
6. **Late Fee Calculation** - Automatic late fees
7. **Payment Plans** - Installment payment support
8. **Receipt Generation** - Automatic receipt generation

## Troubleshooting

### Port Already in Use
```bash
lsof -i :8082
kill -9 <PID>
```

### Database Connection Error
```bash
psql -U postgres -c "CREATE DATABASE billing_db;"
```

## Complete Microservices Architecture

| Service | Port | URL                   |
|---------|------|-----------------------|
| Eureka Server | 8761 | http://localhost:8761 |
| Patient Service | 8081 | http://localhost:8081 |
| Appointment Service | 8083 | http://localhost:8083 |
| Medical Record Service | 8084 | http://localhost:8084 |
| Doctor/Staff Service | 8085 | http://localhost:8085 |
| Billing Service | 8082 | http://localhost:8082 |

Your complete healthcare microservices system is now fully operational! 🎉💰