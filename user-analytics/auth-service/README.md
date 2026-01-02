# Authentication & Authorization Service

A comprehensive microservice for handling authentication and authorization in a healthcare management system.

## Features

- **User Registration & Login** - Support for multiple user types (Patient, Doctor, Admin, Staff, Pharmacist, Receptionist)
- **JWT Token Management** - Secure token generation and validation
- **Role-Based Access Control (RBAC)** - Granular permission system
- **Password Management** - Password reset, change, and security features
- **Account Security** - Account locking after failed attempts, email verification
- **Token Validation Endpoint** - For inter-service communication
- **Spring Cloud Integration** - Eureka service discovery

## Architecture

### Entity Model
```
User
├── Roles (Many-to-Many)
│   └── Permissions (Many-to-Many)
```

### User Types
- `PATIENT` - Healthcare recipients
- `DOCTOR` - Medical practitioners
- `ADMIN` - System administrators
- `STAFF` - General hospital staff
- `PHARMACIST` - Pharmacy personnel
- `RECEPTIONIST` - Front desk staff

### Default Roles
- `ROLE_ADMIN` - Full system access
- `ROLE_DOCTOR` - Medical operations
- `ROLE_PATIENT` - Limited patient access
- `ROLE_STAFF` - Operational tasks
- `ROLE_PHARMACIST` - Prescription management
- `ROLE_RECEPTIONIST` - Appointment and patient check-in

## API Endpoints

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "userType": "PATIENT",
  "roles": ["ROLE_PATIENT"]
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "john_doe",
  "password": "SecurePass123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "userType": "PATIENT",
    "roles": ["ROLE_PATIENT"],
    "permissions": ["APPOINTMENT_CREATE", "APPOINTMENT_READ", ...]
  }
}
```

### Token Validation

#### Validate Token (Body)
```http
POST /api/auth/validate
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

#### Validate Token (Header)
```http
POST /api/auth/validate-header
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

Response:
```json
{
  "valid": true,
  "username": "john_doe",
  "email": "john@example.com",
  "userId": 1,
  "userType": "PATIENT",
  "roles": ["ROLE_PATIENT"],
  "permissions": ["APPOINTMENT_CREATE", "APPOINTMENT_READ", ...],
  "message": "Token is valid"
}
```

### Password Management

#### Request Password Reset
```http
POST /api/auth/password-reset/request
Content-Type: application/json

{
  "email": "john@example.com"
}
```

#### Confirm Password Reset
```http
POST /api/auth/password-reset/confirm
Content-Type: application/json

{
  "token": "reset-token-here",
  "newPassword": "NewSecurePass123"
}
```

#### Change Password
```http
POST /api/auth/password/change
User-Id: 1
Content-Type: application/json

{
  "currentPassword": "OldPassword123",
  "newPassword": "NewPassword123"
}
```

### Email Verification
```http
GET /api/auth/verify-email?token=verification-token-here
```

## Configuration

### Environment Variables

Create an `application-dev.yml` or set environment variables:

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000
  refresh:
    expiration: 604800000

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/authservice_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Database Setup

1. Create the database:
```sql
CREATE DATABASE authservice_db;
```

2. The application will auto-create tables on startup (using JPA)

3. Default roles and permissions are automatically initialized

## Running the Service

### Prerequisites
- Java 21
- PostgreSQL
- Maven
- Eureka Server (for service discovery)

### Steps

1. **Update parent POM** - Add auth-service module:
```xml
<modules>
    <module>demo</module>
    <module>auth-service</module>
    <!-- other modules -->
</modules>
```

2. **Build the project**:
```bash
mvn clean install
```

3. **Run Eureka Server** (if not already running):
```bash
cd eureka-server
mvn spring-boot:run
```

4. **Run Auth Service**:
```bash
cd auth-service
mvn spring-boot:run
```

The service will start on port **8081** and register with Eureka.

## Usage in Other Microservices

### Add Dependency (in other service's pom.xml)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Token Validation Example
```java
@Service
public class AuthClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${auth.service.url:http://auth-service}")
    private String authServiceUrl;
    
    public TokenValidationResponse validateToken(String token) {
        String url = authServiceUrl + "/api/auth/validate";
        
        TokenValidationRequest request = new TokenValidationRequest(token);
        
        return restTemplate.postForObject(
            url, 
            request, 
            TokenValidationResponse.class
        );
    }
}
```

### Security Filter for Microservices
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private AuthClient authClient;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            TokenValidationResponse validation = authClient.validateToken(token);
            
            if (validation.getValid()) {
                // Set authentication in SecurityContext
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        validation.getUsername(), 
                        null, 
                        getAuthorities(validation.getRoles())
                    );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

## Testing

### Integration Tests
```bash
mvn test
```

### Manual Testing with cURL

**Register:**
```bash
curl -X POST http://localhost:8087/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "userType": "PATIENT"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8087/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Test123456"
  }'
```

**Validate Token:**
```bash
curl -X POST http://localhost:8087/api/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "token": "YOUR_JWT_TOKEN_HERE"
  }'
```

## Security Features

1. **Password Encryption** - BCrypt with strength 12
2. **Account Locking** - After 5 failed login attempts
3. **Token Expiration** - 24 hours for access tokens, 7 days for refresh tokens
4. **Email Verification** - Configurable verification flow
5. **Password Reset** - Secure token-based reset with expiration
6. **Two-Factor Authentication** - Framework ready (implementation pending)

## Monitoring

### Actuator Endpoints
- Health: `http://localhost:8087/actuator/health`
- Metrics: `http://localhost:8087/actuator/metrics`
- Info: `http://localhost:8087/actuator/info`

## Error Handling

All errors return a consistent format:
```json
{
  "success": false,
  "message": "Error message here",
  "data": null
}
```

HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request / Validation Error
- `401` - Unauthorized
- `404` - Not Found
- `500` - Internal Server Error

## License

Copyright © 2024 Healthcare Management System