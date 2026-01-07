# Service Startup Troubleshooting Guide

## Issue 1: Duplicate Feign Client Bean

### Error Message:
```
BeanDefinitionOverrideException: Invalid bean definition with name 'auth-service.FeignClientSpecification'
```

### Root Cause:
The AuthServiceClient Feign interface is being registered multiple times.

### Solution Steps:

#### Step 1: Update application.yml (Quick Fix)
Add to `patientservice/src/main/resources/application.yml`:

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
```

#### Step 2: Update Main Application Class
Replace your `PatientServiceApplication.java` with:

```java
package com.example.patientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.common.security.client")
@ComponentScan(basePackages = {
    "com.example.common.security",
    "com.example.patientservice"
})
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

#### Step 3: Rebuild Common Security
```bash
cd common-security
mvn clean install
cd ..
```

#### Step 4: Clean and Rebuild Patient Service
```bash
cd patientservice
mvn clean package
mvn spring-boot:run
```

## Issue 2: Auth Service Not Found

### Error Message:
```
FeignException: 503 Service Unavailable
or
Load balancer does not have available server for client: auth-service
```

### Solution Steps:

#### Check 1: Eureka Server Running
```bash
curl http://localhost:8761/
```

If not running:
```bash
cd eureka-server
mvn spring-boot:run
```

#### Check 2: Auth Service Running and Registered
```bash
curl http://localhost:8761/eureka/apps
```

Look for `<app>AUTH-SERVICE</app>`

If not running:
```bash
cd auth-service
mvn spring-boot:run
```

#### Check 3: Verify Auth Service URL
In `application.yml`:
```yaml
auth:
  service:
    url: http://localhost:8081  # For local
    # url: http://auth-service:8081  # For Docker
```

#### Check 4: Test Auth Service Directly
```bash
curl http://localhost:8081/api/auth/health
```

## Issue 3: Database Connection Failed

### Error Message:
```
PSQLException: Connection refused
or
org.postgresql.util.PSQLException: FATAL: database "patient_db" does not exist
```

### Solution Steps:

#### Step 1: Check PostgreSQL Running
```bash
# Linux/Mac
ps aux | grep postgres

# Windows
tasklist | findstr postgres
```

Start if not running:
```bash
# Linux
sudo systemctl start postgresql

# Mac
brew services start postgresql

# Windows
net start postgresql-x64-15
```

#### Step 2: Create Database
```bash
psql -U postgres
CREATE DATABASE patient_db;
\l
\q
```

#### Step 3: Verify Connection Settings
In `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/patient_db
    username: postgres
    password: postgres  # Change to your password
```

#### Step 4: Test Connection
```bash
psql -U postgres -d patient_db -c "SELECT 1"
```

## Issue 4: Port Already in Use

### Error Message:
```
Web server failed to start. Port 8082 was already in use.
```

### Solution Steps:

#### Option 1: Kill Process Using Port
```bash
# Find process
# Linux/Mac
lsof -i :8082

# Windows
netstat -ano | findstr :8082

# Kill process
# Linux/Mac
kill -9 <PID>

# Windows
taskkill /PID <PID> /F
```

#### Option 2: Change Port
In `application.yml`:
```yaml
server:
  port: 8092  # Use different port
```

## Issue 5: Common Security Library Not Found

### Error Message:
```
Could not resolve dependencies for project: Could not find artifact com.example:common-security:jar:1.0.0
```

### Solution Steps:

#### Step 1: Build and Install Common Security
```bash
cd common-security
mvn clean install -DskipTests
```

Verify installation:
```bash
ls ~/.m2/repository/com/example/common-security/1.0.0/
```

Should see: `common-security-1.0.0.jar`

#### Step 2: Check Parent POM
Ensure `common-security` is in `user-analytics/pom.xml`:
```xml
<modules>
    <module>common-security</module>
    <module>patientservice</module>
    <!-- other modules -->
</modules>
```

#### Step 3: Rebuild from Root
```bash
cd user-analytics
mvn clean install -DskipTests
```

## Issue 6: Component Not Found / Autowiring Failed

### Error Message:
```
NoSuchBeanDefinitionException: No qualifying bean of type 'AuthServiceClient'
```

### Solution Steps:

#### Check 1: Verify @EnableFeignClients
Your main application class should have:
```java
@EnableFeignClients(basePackages = "com.example.common.security.client")
```

#### Check 2: Verify @ComponentScan
```java
@ComponentScan(basePackages = {
    "com.example.common.security",
    "com.example.patientservice"
})
```

#### Check 3: Rebuild Common Security
```bash
cd common-security
mvn clean install
```

#### Check 4: Clean Rebuild Service
```bash
cd patientservice
mvn clean package
```

## Issue 7: JWT Token Validation Fails

### Error Message:
```
401 Unauthorized
or
Invalid JWT signature
```

### Solution Steps:

#### Check 1: Verify Token Format
Token should start with: `eyJhbGciOiJIUzI1NiIs...`

#### Check 2: Test Token with Auth Service
```bash
curl -X POST http://localhost:8081/api/auth/validate \
  -H "Content-Type: application/json" \
  -d '{"token": "YOUR_TOKEN_HERE"}'
```

#### Check 3: Verify JWT Secret Matches
In `auth-service/application.yml` and all services, JWT secret should be the same.

#### Check 4: Check Token Expiration
Tokens expire after 24 hours by default. Login again to get a new token.

## Issue 8: Access Denied (403)

### Error Message:
```
403 Forbidden
Access Denied: Insufficient permissions
```

### Solution Steps:

#### Check 1: Verify User Permissions
```bash
TOKEN="your-token-here"
curl -X POST http://localhost:8081/api/auth/validate \
  -H "Content-Type: application/json" \
  -d "{\"token\": \"$TOKEN\"}" | jq '.permissions'
```

#### Check 2: Check Required Permission
Look at controller method:
```java
@RequirePermission(PermissionConstants.PATIENT_READ)
```

User must have `PATIENT_READ` permission.

#### Check 3: Verify Role Assignments
In auth service database:
```sql
SELECT u.username, r.name as role, p.name as permission
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
JOIN role_permissions rp ON r.id = rp.role_id
JOIN permissions p ON rp.permission_id = p.id
WHERE u.username = 'your-username';
```

## Quick Diagnostic Commands

### Check All Services Status
```bash
# Eureka
curl -s http://localhost:8761/actuator/health | jq

# Auth Service
curl -s http://localhost:8081/actuator/health | jq

# Patient Service
curl -s http://localhost:8082/actuator/health | jq
```

### Check Service Registration
```bash
curl -s http://localhost:8761/eureka/apps | grep -E '<app>|<status>'
```

### Test Full Authentication Flow
```bash
# Register
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "userType": "PATIENT"
  }'

# Login
TOKEN=$(curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Test123456"
  }' | jq -r '.token')

# Use token
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8082/api/patients/me
```

## Startup Order

Services should be started in this order:

1. **PostgreSQL** (if not already running)
2. **Eureka Server** (Port 8761)
3. **Auth Service** (Port 8081)
4. **Other Services** (8082+)

Wait for each service to fully start before starting the next.

## Clean Restart Procedure

If all else fails:

```bash
# Stop all services
# Ctrl+C in all terminal windows

# Clean everything
cd user-analytics
mvn clean

# Rebuild common-security
cd common-security
mvn clean install -DskipTests

# Go back to root
cd ..

# Rebuild all
mvn clean install -DskipTests

# Start in order
# Terminal 1
cd eureka-server && mvn spring-boot:run

# Terminal 2 (wait for Eureka to start)
cd auth-service && mvn spring-boot:run

# Terminal 3 (wait for Auth Service to register)
cd patientservice && mvn spring-boot:run
```

## Enable Debug Logging

Add to `application.yml`:
```yaml
logging:
  level:
    root: INFO
    com.example: DEBUG
    org.springframework.security: DEBUG
    org.springframework.cloud: DEBUG
    feign: DEBUG
```

## Get Help

If you're still stuck:

1. Check application logs in detail
2. Enable debug logging
3. Test each component individually
4. Verify all configuration files
5. Check network connectivity between services

## Verification Checklist

- [ ] PostgreSQL is running
- [ ] All databases exist (analytics_db, patient_db, etc.)
- [ ] Eureka Server is running on 8761
- [ ] Auth Service is running on 8081
- [ ] Auth Service is registered in Eureka
- [ ] Patient Service can reach Auth Service
- [ ] Common Security library is built and installed
- [ ] Bean override is enabled in application.yml
- [ ] @EnableFeignClients is correctly configured
- [ ] Can login and get JWT token
- [ ] Token validation works
- [ ] Protected endpoints respond correctly