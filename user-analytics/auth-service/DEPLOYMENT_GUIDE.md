# Authentication Service - Deployment Guide

## 📋 Table of Contents
1. [Quick Start](#quick-start)
2. [Project Structure](#project-structure)
3. [Prerequisites](#prerequisites)
4. [Local Development Setup](#local-development-setup)
5. [Docker Deployment](#docker-deployment)
6. [Production Deployment](#production-deployment)
7. [Testing](#testing)
8. [Monitoring](#monitoring)
9. [Troubleshooting](#troubleshooting)

## 🚀 Quick Start

### Option 1: Local Development (Fastest)

```bash
# 1. Create database
psql -U postgres
CREATE DATABASE authservice_db;
\q

# 2. Build and run
cd auth-service
mvn clean install
mvn spring-boot:run
```

### Option 2: Docker Compose (Recommended for Testing)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f auth-service

# Stop all services
docker-compose down
```

## 📁 Project Structure

```
healthcare-management/
├── user-analytics/              # Parent project
│   ├── pom.xml                 # Parent POM
│   ├── auth-service/           # ✨ NEW Authentication Service
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/example/authservice/
│   │   │   │   │   ├── AuthServiceApplication.java
│   │   │   │   │   ├── config/
│   │   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   │   └── DatabaseInitializer.java
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── AuthController.java
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── AuthRequest.java
│   │   │   │   │   │   ├── AuthResponse.java
│   │   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   │   └── ...
│   │   │   │   │   ├── exception/
│   │   │   │   │   │   ├── AuthException.java
│   │   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── Role.java
│   │   │   │   │   │   ├── Permission.java
│   │   │   │   │   │   └── UserType.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   │   ├── RoleRepository.java
│   │   │   │   │   │   └── PermissionRepository.java
│   │   │   │   │   ├── security/
│   │   │   │   │   │   └── JwtUtil.java
│   │   │   │   │   └── service/
│   │   │   │   │       └── AuthService.java
│   │   │   │   └── resources/
│   │   │   │       ├── application.yml
│   │   │   │       └── db/changelog/
│   │   │   │           └── db.changelog-master.xml
│   │   │   └── test/
│   │   │       └── java/com/example/authservice/
│   │   │           ├── service/
│   │   │           │   └── AuthServiceTest.java
│   │   │           └── controller/
│   │   │               └── AuthControllerIntegrationTest.java
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── README.md
│   ├── demo/                   # Existing module
│   ├── eureka-server/         # Service discovery
│   ├── patientservice/
│   ├── appointment-service/
│   ├── medicalrecordservice/
│   ├── doctorservice/
│   ├── billingservice/
│   └── prescriptionservice/
├── docker-compose.yml
├── init-db.sql
└── Auth-Service-Postman-Collection.json
```

## ✅ Prerequisites

### Required Software
- **Java 21** - [Download](https://adoptium.net/)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 15+** - [Download](https://www.postgresql.org/download/)
- **Docker & Docker Compose** (Optional) - [Download](https://www.docker.com/products/docker-desktop/)

### Verify Installation
```bash
java -version    # Should show Java 21
mvn -version     # Should show Maven 3.9+
psql --version   # Should show PostgreSQL 15+
docker --version # (Optional)
```

## 🛠️ Local Development Setup

### Step 1: Database Setup

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE authservice_db;

# Verify
\l

# Exit
\q
```

### Step 2: Update Parent POM

Ensure `auth-service` is listed in the parent `pom.xml`:

```xml
<modules>
    <module>demo</module>
    <module>auth-service</module>  <!-- ✨ Add this -->
    <module>patientservice</module>
    <!-- ... other modules -->
</modules>
```

### Step 3: Configure Application

Edit `auth-service/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/authservice_db
    username: postgres
    password: YOUR_PASSWORD_HERE  # ⚠️ Change this

jwt:
  secret: YOUR_SECRET_KEY_HERE    # ⚠️ Change this (min 256 bits)
```

### Step 4: Build Project

```bash
# From project root
cd user-analytics
mvn clean install

# Or build only auth-service
cd auth-service
mvn clean install
```

### Step 5: Run the Service

```bash
cd auth-service
mvn spring-boot:run
```

The service will start on **http://localhost:8087**

### Step 6: Verify Service is Running

```bash
# Health check
curl http://localhost:8087/api/auth/health

# Expected response: "Auth Service is running"
```

### Step 7: Test Registration

```bash
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "firstName": "Test",
    "lastName": "User",
    "userType": "PATIENT"
  }'
```

## 🐳 Docker Deployment

### Build Docker Image

```bash
# Build auth-service image
cd auth-service
docker build -t auth-service:latest .
```

### Run with Docker Compose

```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f auth-service

# Stop services
docker-compose down

# Clean up volumes
docker-compose down -v
```

### Service URLs (Docker)
- **Eureka Dashboard**: http://localhost:8761
- **Patient Service**: http://localhost:8081
- **Billing Service**: http://localhost:8082
- **Appointment Service**: http://localhost:8083
- **Medical Record Service**: http://localhost:8084
- **Doctor Service**: http://localhost:8085
- **Prescription Service**: http://localhost:8086
- **Auth Service**: http://localhost:8087

## 🚢 Production Deployment

### Environment Variables

Create a `.env` file:

```bash
# Database
DB_HOST=your-db-host
DB_PORT=5432
DB_NAME=authservice_db
DB_USERNAME=your-db-user
DB_PASSWORD=your-secure-password

# JWT
JWT_SECRET=your-very-secure-secret-key-minimum-256-bits-long

# Eureka
EUREKA_URL=http://eureka-server:8761/eureka/

# Application
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8087
```

### Production Configuration

Create `application-prod.yml`:

```yaml
server:
  port: ${SERVER_PORT:8087}

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:authservice_db}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  
  jpa:
    hibernate:
      ddl-auto: validate  # Don't auto-create in production
    show-sql: false

jwt:
  secret: ${JWT_SECRET}
  expiration: 3600000    # 1 hour
  refresh:
    expiration: 604800000  # 7 days

logging:
  level:
    root: INFO
    com.example.authservice: INFO
```

### Security Checklist

- [ ] Change default database passwords
- [ ] Use strong JWT secret (minimum 256 bits)
- [ ] Enable HTTPS/TLS
- [ ] Configure firewall rules
- [ ] Set up database backups
- [ ] Enable monitoring and logging
- [ ] Configure rate limiting
- [ ] Set up email service for password resets
- [ ] Review and update CORS settings
- [ ] Enable audit logging

## 🧪 Testing

### Run Unit Tests

```bash
cd auth-service
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Import Postman Collection

1. Open Postman
2. Click **Import**
3. Select `Auth-Service-Postman-Collection.json`
4. Set `base_url` variable to `http://localhost:8087`
5. Run requests in order:
    - Register Patient
    - Login (saves token automatically)
    - Validate Token
    - Other endpoints

### Manual Testing Flow

```bash
# 1. Register
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"Pass123456","userType":"PATIENT"}'

# 2. Login
TOKEN=$(curl -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"john","password":"Pass123456"}' \
  | jq -r '.token')

# 3. Validate
curl -X POST http://localhost:8087/api/auth/validate-header \
  -H "Authorization: Bearer $TOKEN"
```

## 📊 Monitoring

### Actuator Endpoints

```bash
# Health
curl http://localhost:8087/actuator/health

# Metrics
curl http://localhost:8087/actuator/metrics

# Info
curl http://localhost:8087/actuator/info
```

### Logs

```bash
# View logs (Docker)
docker-compose logs -f auth-service

# View logs (Local)
tail -f logs/auth-service.log
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Database Connection Failed
```
Error: PSQLException: Connection refused
```
**Solution:**
- Check PostgreSQL is running: `pg_isready`
- Verify connection string in `application.yml`
- Check firewall rules

#### 2. Port Already in Use
```
Error: Port 8087 already in use
```
**Solution:**
```bash
# Find process
lsof -i :8087

# Kill process
kill -9 <PID>

# Or change port in application.yml
```

#### 3. JWT Token Invalid
```
Error: Invalid JWT signature
```
**Solution:**
- Ensure same JWT secret across all services
- Check token hasn't expired
- Verify token format (should start with "Bearer ")

#### 4. Eureka Registration Failed
```
Error: Cannot register with Eureka
```
**Solution:**
- Ensure Eureka server is running
- Check `eureka.client.serviceUrl.defaultZone` configuration
- Verify network connectivity

#### 5. Maven Build Failure
```
Error: Could not resolve dependencies
```
**Solution:**
```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Rebuild
mvn clean install -U
```

### Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.example.authservice: DEBUG
    org.springframework.security: DEBUG
```

## 📞 Support

### Resources
- [README.md](auth-service/README.md) - Full API documentation
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [JWT.io](https://jwt.io) - JWT debugger

### Getting Help
1. Check logs first
2. Review configuration
3. Test with Postman collection
4. Check Eureka dashboard for service registration

## 🎯 Next Steps

1. **Integrate with Other Services**
    - Add JWT validation filter to other microservices
    - Configure service-to-service authentication

2. **Enhance Security**
    - Implement 2FA
    - Add rate limiting
    - Set up WAF (Web Application Firewall)

3. **Add Features**
    - Email verification service
    - Password complexity rules
    - Session management
    - OAuth2 integration

4. **Monitoring & Logging**
    - Set up ELK stack
    - Configure Prometheus/Grafana
    - Add distributed tracing (Zipkin/Jaeger)

## ✅ Summary

You now have a fully functional Authentication & Authorization microservice with:

✅ User registration and login  
✅ JWT token management  
✅ Role-based access control (RBAC)  
✅ Password management (reset, change)  
✅ Token validation for other services  
✅ Eureka service discovery integration  
✅ Comprehensive error handling  
✅ Health checks and monitoring  
✅ Docker support  
✅ Complete test suite  
✅ API documentation

**Service is ready for integration with other microservices!** 🚀