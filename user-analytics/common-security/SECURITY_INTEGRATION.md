# 🔐 Microservices Security Integration - Quick Reference

## 📦 What Was Created

### 1. Common Security Library (`common-security`)
A reusable library that provides JWT authentication and authorization for all microservices.

**Components:**
- ✅ **DTOs**: TokenValidationRequest, TokenValidationResponse, UserPrincipal
- ✅ **Feign Client**: AuthServiceClient for calling auth service
- ✅ **JWT Filter**: JwtAuthenticationFilter for request authentication
- ✅ **Security Config**: MicroserviceSecurityConfig with auto-configuration
- ✅ **Annotations**: @RequirePermission, @RequireRole for method security
- ✅ **Utilities**: SecurityUtils for accessing current user info
- ✅ **Constants**: PermissionConstants, RoleConstants

### 2. Integration Examples
- ✅ Patient Service integration
- ✅ Appointment Service integration
- ✅ Medical Records Service integration
- ✅ Prescription Service integration
- ✅ Billing Service integration

### 3. Automation Scripts
- ✅ `setup-security-integration.sh` - Automated setup script
- ✅ `test-security-integration.sh` - Integration testing script
- ✅ `docker-compose-test.yml` - Docker test environment

### 4. Documentation
- ✅ Complete integration guide
- ✅ Service-specific examples
- ✅ Troubleshooting guide
- ✅ Testing procedures

## 🚀 Quick Start (3 Steps)

### Option 1: Automated Setup

```bash
# 1. Make script executable
chmod +x setup-security-integration.sh

# 2. Run setup script
./setup-security-integration.sh

# 3. Test integration
./test-security-integration.sh
```

### Option 2: Manual Setup

```bash
# 1. Build common-security
cd common-security
mvn clean install
cd ..

# 2. Update parent POM (add common-security module)
# Edit user-analytics/pom.xml

# 3. Add dependency to each service
# Add to each service's pom.xml:
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-security</artifactId>
    <version>1.0.0</version>
</dependency>

# 4. Build all services
mvn clean install

# 5. Start services
# Use docker-compose or run individually
```

## 📋 Integration Checklist

### For Each Microservice:

#### 1. POM Configuration
```xml
✅ Add common-security dependency
✅ Include spring-cloud-starter-openfeign
✅ Include spring-boot-starter-security
```

#### 2. Application Configuration
```yaml
✅ Add auth.service.url
✅ Configure Feign clients
✅ Set up Eureka client
```

#### 3. Main Application Class
```java
✅ Add @EnableFeignClients with security package
✅ Add @ComponentScan with security package
✅ Keep @EnableDiscoveryClient
```

#### 4. Controller Security
```java
✅ Add @RequirePermission annotations
✅ Use SecurityUtils for user context
✅ Implement resource ownership checks
✅ Add @PreAuthorize for role-based access
```

#### 5. Service Layer
```java
✅ Use SecurityUtils.getCurrentUserId()
✅ Add audit fields (createdBy, updatedBy)
✅ Implement ownership validation
```

## 🎯 Usage Patterns

### Pattern 1: Simple Permission Check
```java
@GetMapping("/{id}")
@RequirePermission(PermissionConstants.PATIENT_READ)
public PatientDTO getPatient(@PathVariable Long id) {
    return patientService.getPatientById(id);
}
```

### Pattern 2: Role-Based Access
```java
@GetMapping
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR')")
public List<PatientDTO> getAllPatients() {
    return patientService.getAllPatients();
}
```

### Pattern 3: Ownership Validation
```java
@PutMapping("/{id}")
@RequirePermission(PermissionConstants.PATIENT_UPDATE)
public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO dto) {
    if (SecurityUtils.isPatient() && !SecurityUtils.isOwner(getPatientUserId(id))) {
        throw new AccessDeniedException("Cannot update other patient's records");
    }
    return patientService.updatePatient(id, dto);
}
```

### Pattern 4: Conditional Logic Based on Role
```java
public List<AppointmentDTO> getAppointments() {
    if (SecurityUtils.isPatient()) {
        Long userId = SecurityUtils.getCurrentUserId().orElseThrow();
        return appointmentService.getPatientAppointments(userId);
    } else if (SecurityUtils.isDoctor()) {
        Long userId = SecurityUtils.getCurrentUserId().orElseThrow();
        return appointmentService.getDoctorAppointments(userId);
    } else {
        return appointmentService.getAllAppointments();
    }
}
```

### Pattern 5: Service Layer Audit
```java
@Transactional
public PatientDTO createPatient(PatientDTO dto) {
    Patient patient = new Patient();
    // ... set fields
    
    // Automatically set audit fields
    patient.setCreatedBy(SecurityUtils.getCurrentUsername().orElse("system"));
    patient.setCreatedAt(LocalDateTime.now());
    patient.setUserId(SecurityUtils.getCurrentUserId().orElse(null));
    
    return patientRepository.save(patient);
}
```

## 🔍 Testing Workflow

### 1. Start Services
```bash
# Using Docker Compose
docker-compose -f docker-compose-test.yml up -d

# Or start individually
# Terminal 1: Eureka
cd eureka-server && mvn spring-boot:run

# Terminal 2: Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 3: Patient Service
cd patientservice && mvn spring-boot:run
```

### 2. Register Users
```bash
# Register Patient
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "patient1",
    "email": "patient1@example.com",
    "password": "Patient123",
    "userType": "PATIENT"
  }'

# Register Doctor
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor1",
    "email": "doctor1@example.com",
    "password": "Doctor123",
    "userType": "DOCTOR"
  }'
```

### 3. Login and Get Token
```bash
TOKEN=$(curl -s -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "patient1",
    "password": "Patient123"
  }' | jq -r '.token')

echo "Token: $TOKEN"
```

```bash
TOKEN=$(curl -s -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "doctor1",
    "password": "Doctor123"
  }' | jq -r '.token')

echo "Token: $TOKEN"
```

### 4. Test Protected Endpoints
```bash
# With token (should succeed)
curl -X GET http://localhost:8081/patient-service/api/v1/patients/1 \
  -H "Authorization: Bearer $TOKEN"

# Without token (should fail with 401)
curl -X GET http://localhost:8081/patient-service/api/v1/patients/1

# With token but insufficient permissions (should fail with 403)
curl -X DELETE http://localhost:8081/patient-service/api/v1/patients/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 🐛 Common Issues & Solutions

### Issue 1: "Auth service is unavailable"
```bash
# Check auth service is running
curl http://localhost:8081/api/auth/health

# Check Eureka registration
curl http://localhost:8761/eureka/apps

# Verify Feign client configuration
# In application.yml: auth.service.url should point to auth-service
```

### Issue 2: "Access Denied" (403)
```bash
# Check user permissions
curl -X POST http://localhost:8081/api/auth/validate \
  -H "Content-Type: application/json" \
  -d "{\"token\": \"$TOKEN\"}" | jq '.permissions'

# Verify @RequirePermission matches user's permissions
```

### Issue 3: "Unauthorized" (401)
```bash
# Validate token format
echo $TOKEN

# Should start with: eyJhbGciOiJIUzI1NiIs...

# Test token directly with auth service
curl -X POST http://localhost:8081/api/auth/validate-header \
  -H "Authorization: Bearer $TOKEN"
```

### Issue 4: Bean Creation Failed
```bash
# Rebuild common-security
cd common-security
mvn clean install

# Clear and rebuild service
cd ../patientservice
mvn clean package
```

## 📊 Service Port Map

| Service | Port | Health Check |
|---------|------|--------------|
| Eureka Server | 8761 | http://localhost:8761/actuator/health |
| Auth Service | 8081 | http://localhost:8081/api/auth/health |
| Patient Service | 8082 | http://localhost:8082/actuator/health |
| Appointment Service | 8083 | http://localhost:8083/actuator/health |
| Medical Record Service | 8084 | http://localhost:8084/actuator/health |
| Doctor Service | 8085 | http://localhost:8085/actuator/health |
| Billing Service | 8086 | http://localhost:8086/actuator/health |
| Prescription Service | 8087 | http://localhost:8087/actuator/health |

## 🔑 Permission Matrix

| Role | Permissions |
|------|-------------|
| **ADMIN** | ALL permissions |
| **DOCTOR** | PATIENT_READ, PATIENT_UPDATE<br>APPOINTMENT_READ, APPOINTMENT_UPDATE<br>MEDICAL_RECORD_* (all)<br>PRESCRIPTION_* (all) |
| **PATIENT** | APPOINTMENT_CREATE, APPOINTMENT_READ<br>MEDICAL_RECORD_READ<br>PRESCRIPTION_READ<br>BILLING_READ |
| **STAFF** | PATIENT_* (except DELETE)<br>APPOINTMENT_* (except DELETE)<br>BILLING_* (except DELETE) |
| **PHARMACIST** | PRESCRIPTION_READ, PRESCRIPTION_UPDATE |
| **RECEPTIONIST** | PATIENT_CREATE, PATIENT_READ<br>APPOINTMENT_* (except DELETE) |

## 📚 Key Files Reference

```
user-analytics/
├── common-security/              ← New security library
│   ├── src/main/java/com/example/common/security/
│   │   ├── client/              ← Auth service clients
│   │   ├── config/              ← Auto-configuration
│   │   ├── dto/                 ← Data transfer objects
│   │   ├── filter/              ← JWT authentication filter
│   │   ├── annotation/          ← Custom annotations
│   │   ├── aspect/              ← Permission checking
│   │   ├── constants/           ← Permission/Role constants
│   │   └── util/                ← SecurityUtils
│   └── pom.xml
├── auth-service/                ← Authentication service
├── patientservice/              ← Example integrated service
│   ├── src/main/java/.../controller/
│   │   └── PatientController.java  ← Uses @RequirePermission
│   ├── src/main/java/.../service/
│   │   └── PatientService.java     ← Uses SecurityUtils
│   ├── src/main/resources/
│   │   └── application.yml         ← Auth config
│   └── pom.xml                     ← common-security dependency
├── setup-security-integration.sh   ← Setup automation
├── test-security-integration.sh    ← Testing script
└── docker-compose-test.yml         ← Test environment
```

## 🎓 Learning Resources

1. **Review Integration Guide** - Full step-by-step instructions
2. **Study Patient Service Example** - Complete working example
3. **Test with Postman** - Use provided collection
4. **Check Logs** - Understand authentication flow
5. **Read Spring Security Docs** - Deep dive into security concepts

## ✅ Verification Steps

After integration, verify:

```bash
# 1. All services registered with Eureka
curl http://localhost:8761/eureka/apps | grep -o '<app>[^<]*</app>'

# 2. Auth service responding
curl http://localhost:8081/api/auth/health

# 3. Can register user
curl -X POST http://localhost:8081/api/auth/register -H "Content-Type: application/json" -d '...'

# 4. Can login and get token
curl -X POST http://localhost:8081/api/auth/login -H "Content-Type: application/json" -d '...'

# 5. Token validation works
curl -X POST http://localhost:8081/api/auth/validate -H "Content-Type: application/json" -d '{"token":"..."}'

# 6. Protected endpoints require authentication
curl http://localhost:8082/api/patients/1  # Should return 401

# 7. Protected endpoints accept valid tokens
curl -H "Authorization: Bearer TOKEN" http://localhost:8082/api/patients/me
```

## 🚀 Production Checklist

Before deploying:

- [ ] Change JWT secret to secure random value
- [ ] Use environment variables for sensitive data
- [ ] Enable HTTPS/TLS
- [ ] Configure proper CORS origins
- [ ] Set up centralized logging
- [ ] Enable monitoring and alerting
- [ ] Implement rate limiting
- [ ] Add request tracing
- [ ] Set up backup and recovery
- [ ] Document security policies
- [ ] Perform security audit
- [ ] Load test authentication flow

## 📞 Support

- **Documentation**: See `integration-guide.md` for detailed instructions
- **Examples**: Check `patient_service_integration` artifact
- **Issues**: Review troubleshooting section
- **Questions**: Refer to Spring Security documentation

---

**Status**: ✅ Ready for integration
**Version**: 1.0.0
**Last Updated**: January 2026

**Next**: Follow the integration guide to complete setup for all services!