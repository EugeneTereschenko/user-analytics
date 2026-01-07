# Microservices Security Integration Guide

## 📋 Overview

This guide shows you how to integrate the common security library with all your microservices to enable JWT authentication and authorization.

## 🏗️ Architecture

```
┌─────────────────┐
│  Auth Service   │ ◄──── Issues JWT Tokens
│   (Port 8087)   │
└────────┬────────┘
         │
         │ Validates Tokens
         │
    ┌────▼────┐
    │  Eureka │
    │ Server  │
    └────┬────┘
         │
         │ Service Discovery
         │
    ┌────▼─────────────────────────────────┐
    │                                       │
┌───▼────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
│Patient │  │Appointment│  │ Medical  │  │ Doctor   │
│Service │  │ Service   │  │ Record   │  │ Service  │
└────────┘  └──────────┘  └──────────┘  └──────────┘
    │            │              │              │
    └────────────┴──────────────┴──────────────┘
                     │
            Uses Common Security Library
                (JWT Validation)
```

## 📦 Step 1: Create Common Security Library

### 1.1 Update Parent POM

Add `common-security` module to `user-analytics/pom.xml`:

```xml
<modules>
    <module>demo</module>
    <module>auth-service</module>
    <module>common-security</module>  <!-- ✨ Add this -->
    <module>patientservice</module>
    <module>appointment-service</module>
    <module>medicalrecordservice</module>
    <module>doctorservice</module>
    <module>billingservice</module>
    <module>prescriptionservice</module>
    <module>eureka-server</module>
</modules>
```

### 1.2 Create Common Security Module

Create directory structure:

```bash
mkdir -p common-security/src/main/java/com/example/common/security
```

Copy all files from the artifacts:
- `common_security_pom` → `common-security/pom.xml`
- `common_security_dto` → DTOs
- `common_security_client` → Client classes
- `common_security_filter` → Filter classes
- `common_security_config` → Configuration classes
- `common_security_utils` → Utility classes

### 1.3 Build Common Security Library

```bash
cd common-security
mvn clean install
```

This installs the library to your local Maven repository.

## 🔧 Step 2: Integrate with Each Microservice

### 2.1 Update Service POM

Add to each service's `pom.xml` (e.g., `patientservice/pom.xml`):

```xml
<dependencies>
    <!-- ✨ Add Common Security Library -->
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>common-security</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- Existing dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    
    <!-- Other dependencies -->
</dependencies>
```

### 2.2 Update Application Configuration

Add to `application.yml` for each service:

```yaml
server:
  port: 8082  # Different for each service

spring:
  application:
    name: patient-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

# ✨ Add Auth Service Configuration
auth:
  service:
    url: http://auth-service:8087

# ✨ Feign Configuration
feign:
  circuitbreaker:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: BASIC
```

### 2.3 Update Main Application Class

Modify each service's main class (e.g., `PatientServiceApplication.java`):

```java
package com.example.patientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
    "com.example.common.security.client",  // ✨ Scan security clients
    "com.example.patientservice.client"
})
@ComponentScan(basePackages = {
    "com.example.common.security",         // ✨ Scan security components
    "com.example.patientservice"
})
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

### 2.4 Remove Existing Security Configuration

If your service has its own `SecurityConfig` class, **remove or replace it** with imports from the common library.

The common security library automatically configures:
- JWT Authentication Filter
- Security Filter Chain
- CORS Configuration
- Exception Handlers

## 🎯 Step 3: Secure Your Controllers

### Method 1: Using @RequirePermission Annotation

```java
package com.example.patientservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.example.common.security.util.SecurityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.PATIENT_READ)
    public PatientDTO getPatient(@PathVariable Long id) {
        // Only users with PATIENT_READ permission can access
        // ...
    }

    @PostMapping
    @RequirePermission(PermissionConstants.PATIENT_CREATE)
    public PatientDTO createPatient(@RequestBody PatientDTO dto) {
        // Only users with PATIENT_CREATE permission can access
        // ...
    }

    @PutMapping("/{id}")
    @RequirePermission(PermissionConstants.PATIENT_UPDATE)
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO dto) {
        // Check if patient owns the record
        if (SecurityUtils.isPatient() && !SecurityUtils.isOwner(getPatientUserId(id))) {
            throw new AccessDeniedException("Cannot update other patient's records");
        }
        // ...
    }
}
```

### Method 2: Using @PreAuthorize (Spring Security)

```java
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public List<PatientDTO> getAllPatients() {
        // Only admins, doctors, and staff can view all patients
        // ...
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePatient(@PathVariable Long id) {
        // Only admins can delete patients
        // ...
    }
}
```

### Method 3: Using SecurityUtils Programmatically

```java
import com.example.common.security.util.SecurityUtils;

@Service
public class PatientService {

    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Patient not found"));
        
        // Patients can only view their own records
        if (SecurityUtils.isPatient()) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
            
            if (!patient.getUserId().equals(currentUserId)) {
                throw new AccessDeniedException("Cannot access other patient's records");
            }
        }
        
        return mapToDTO(patient);
    }
}
```

## 📝 Step 4: Service-Specific Examples

### 4.1 Patient Service

**Permissions Used:**
- `PATIENT_CREATE` - Create patient records
- `PATIENT_READ` - Read patient records
- `PATIENT_UPDATE` - Update patient records
- `PATIENT_DELETE` - Delete patient records

**Controller Example:**

```java
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @RequirePermission(PermissionConstants.PATIENT_CREATE)
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO dto) {
        return ResponseEntity.ok(patientService.createPatient(dto));
    }

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.PATIENT_READ)
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<PatientDTO> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId()
            .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }
}
```

### 4.2 Appointment Service

**Permissions Used:**
- `APPOINTMENT_CREATE` - Create appointments
- `APPOINTMENT_READ` - Read appointments
- `APPOINTMENT_UPDATE` - Update appointments
- `APPOINTMENT_DELETE` - Delete appointments

**Controller Example:**

```java
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @RequirePermission(PermissionConstants.APPOINTMENT_CREATE)
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        // Patients can only create appointments for themselves
        if (SecurityUtils.isPatient()) {
            Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            dto.setPatientUserId(userId);
        }
        return ResponseEntity.ok(appointmentService.createAppointment(dto));
    }

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.APPOINTMENT_READ)
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        
        // Patients can only view their own appointments
        if (SecurityUtils.isPatient()) {
            Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            if (!appointment.getPatientUserId().equals(userId)) {
                throw new AccessDeniedException("Cannot view other patient's appointments");
            }
        }
        
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        // Only doctors can view their appointments
        if (SecurityUtils.isDoctor()) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            if (!doctorId.equals(currentUserId)) {
                throw new AccessDeniedException("Cannot view other doctor's appointments");
            }
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }
}
```

### 4.3 Medical Records Service

**Permissions Used:**
- `MEDICAL_RECORD_CREATE` - Create medical records
- `MEDICAL_RECORD_READ` - Read medical records
- `MEDICAL_RECORD_UPDATE` - Update medical records
- `MEDICAL_RECORD_DELETE` - Delete medical records

**Controller Example:**

```java
@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @RequirePermission(PermissionConstants.MEDICAL_RECORD_CREATE)
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO dto) {
        // Only doctors can create medical records
        dto.setCreatedByUserId(SecurityUtils.getCurrentUserId().orElse(null));
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(dto));
    }

    @GetMapping("/patient/{patientId}")
    @RequirePermission(PermissionConstants.MEDICAL_RECORD_READ)
    public ResponseEntity<List<MedicalRecordDTO>> getPatientMedicalRecords(@PathVariable Long patientId) {
        // Patients can only view their own records
        if (SecurityUtils.isPatient()) {
            Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            if (!medicalRecordService.isPatientOwnedByUser(patientId, userId)) {
                throw new AccessDeniedException("Cannot access other patient's records");
            }
        }
        return ResponseEntity.ok(medicalRecordService.getRecordsByPatientId(patientId));
    }
}
```

### 4.4 Prescription Service

**Permissions Used:**
- `PRESCRIPTION_CREATE` - Create prescriptions
- `PRESCRIPTION_READ` - Read prescriptions
- `PRESCRIPTION_UPDATE` - Update prescriptions

**Controller Example:**

```java
@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @RequirePermission(PermissionConstants.PRESCRIPTION_CREATE)
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody PrescriptionDTO dto) {
        dto.setPrescribedByUserId(SecurityUtils.getCurrentUserId().orElse(null));
        return ResponseEntity.ok(prescriptionService.createPrescription(dto));
    }

    @PutMapping("/{id}/dispense")
    @RequirePermission(PermissionConstants.PRESCRIPTION_UPDATE)
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<PrescriptionDTO> dispensePrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.dispensePrescription(id));
    }

    @GetMapping("/patient/{patientId}")
    @RequirePermission(PermissionConstants.PRESCRIPTION_READ)
    public ResponseEntity<List<PrescriptionDTO>> getPatientPrescriptions(@PathVariable Long patientId) {
        // Patients can only view their own prescriptions
        if (SecurityUtils.isPatient()) {
            Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            if (!prescriptionService.isPatientOwnedByUser(patientId, userId)) {
                throw new AccessDeniedException("Cannot access other patient's prescriptions");
            }
        }
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }
}
```

### 4.5 Billing Service

**Permissions Used:**
- `BILLING_CREATE` - Create bills
- `BILLING_READ` - Read bills
- `BILLING_UPDATE` - Update bills

**Controller Example:**

```java
@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping
    @RequirePermission(PermissionConstants.BILLING_CREATE)
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO dto) {
        return ResponseEntity.ok(billingService.createBill(dto));
    }

    @GetMapping("/patient/{patientId}")
    @RequirePermission(PermissionConstants.BILLING_READ)
    public ResponseEntity<List<BillDTO>> getPatientBills(@PathVariable Long patientId) {
        // Patients can only view their own bills
        if (SecurityUtils.isPatient()) {
            Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
            if (!billingService.isPatientOwnedByUser(patientId, userId)) {
                throw new AccessDeniedException("Cannot access other patient's bills");
            }
        }
        return ResponseEntity.ok(billingService.getBillsByPatient(patientId));
    }

    @PutMapping("/{id}/pay")
    @RequirePermission(PermissionConstants.BILLING_UPDATE)
    public ResponseEntity<BillDTO> payBill(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.payBill(id));
    }
}
```

## 🧪 Step 5: Testing

### 5.1 Test Registration and Login

```bash
# 1. Register a patient
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "patient1",
    "email": "patient1@example.com",
    "password": "Patient123",
    "firstName": "John",
    "lastName": "Doe",
    "userType": "PATIENT"
  }'

# 2. Login
TOKEN=$(curl -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "patient1",
    "password": "Patient123"
  }' | jq -r '.token')

echo "Token: $TOKEN"
```

### 5.2 Test Protected Endpoints

```bash
# Create a patient (requires authentication)
curl -X POST http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane@example.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-01-01",
    "gender": "FEMALE",
    "address": "123 Main St"
  }'

# Get patient details
curl -X GET http://localhost:8081/api/patients/1 \
  -H "Authorization: Bearer $TOKEN"

# Try to access without token (should fail)
curl -X GET http://localhost:8081/api/patients/1

# Expected response: 401 Unauthorized
```

### 5.3 Test Permission-Based Access

```bash
# Login as doctor
DOCTOR_TOKEN=$(curl -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "doctor1",
    "password": "Doctor123"
  }' | jq -r '.token')

# Doctor can view all patients
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $DOCTOR_TOKEN"

# Patient cannot view all patients (should fail with 403)
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN"
```

## 🔧 Step 6: Troubleshooting

### Issue 1: "Auth service is unavailable"

**Symptoms:**
```
Token validation failed: Authentication service is temporarily unavailable
```

**Solutions:**
1. Ensure Auth Service is running: `curl http://localhost:8087/api/auth/health`
2. Check Eureka dashboard: `http://localhost:8761`
3. Verify service registration in Eureka
4. Check application.yml has correct `auth.service.url`

### Issue 2: "Access Denied" or 403 Forbidden

**Symptoms:**
```json
{
  "success": false,
  "message": "Access Denied: Insufficient permissions"
}
```

**Solutions:**
1. Check user has required permissions
2. Verify role assignments in auth service
3. Check @RequirePermission or @PreAuthorize annotations
4. Debug with: `log.info("User permissions: {}", SecurityUtils.getCurrentUser().getPermissions())`

### Issue 3: "User not authenticated" or 401 Unauthorized

**Symptoms:**
```json
{
  "success": false,
  "message": "Unauthorized: Full authentication is required"
}
```

**Solutions:**
1. Verify token is being sent: `Authorization: Bearer <token>`
2. Check token hasn't expired
3. Verify JWT secret matches between auth service and other services
4. Test token validation: `POST http://localhost:8087/api/auth/validate`

### Issue 4: Feign Client Not Found

**Symptoms:**
```
Error creating bean with name 'authServiceClient'
```

**Solutions:**
1. Add `@EnableFeignClients` to main application class
2. Ensure correct base packages in `@EnableFeignClients`
3. Verify common-security dependency in pom.xml
4. Run `mvn clean install` on common-security module

## 📊 Step 7: Monitoring

### Enable Actuator for All Services

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Add to `application.yml`:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

### Monitor Security Events

Add custom logging:
```java
@Slf4j
@Aspect
@Component
public class SecurityAuditAspect {
    
    @Around("@annotation(requirePermission)")
    public Object logPermissionCheck(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) 
            throws Throwable {
        String username = SecurityUtils.getCurrentUsername().orElse("anonymous");
        String method = joinPoint.getSignature().getName();
        
        log.info("User {} attempting to access method {} requiring permissions: {}",
                username, method, String.join(", ", requirePermission.value()));
        
        return joinPoint.proceed();
    }
}
```

## ✅ Checklist

Before deploying to production:

- [ ] All services have common-security dependency
- [ ] All services register with Eureka
- [ ] Auth service URL configured in all services
- [ ] Controllers have appropriate @RequirePermission annotations
- [ ] Tested all endpoints with different user roles
- [ ] JWT secret is secure and consistent across services
- [ ] Error handling is implemented
- [ ] Logging is configured for security events
- [ ] Health checks are working
- [ ] CORS is properly configured

## 🚀 Next Steps

1. **Add Refresh Token Support**
    - Implement token refresh endpoint
    - Handle token expiration gracefully

2. **Add Rate Limiting**
    - Protect against brute force attacks
    - Use Redis for distributed rate limiting

3. **Add Audit Logging**
    - Log all security events
    - Track failed authentication attempts

4. **Add API Gateway**
    - Centralize authentication
    - Add request routing
    - Implement circuit breakers

5. **Add Monitoring & Alerting**
    - Track authentication failures
    - Monitor service health
    - Set up alerts for security events

## 📚 Additional Resources

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Feign Client Documentation](https://cloud.spring.io/spring-cloud-openfeign/reference/html/)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)
- [Microservices Security Patterns](https://microservices.io/patterns/security/access-token.html)

---

**Congratulations!** 🎉 Your microservices are now secured with JWT authentication and role-based authorization!