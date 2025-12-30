# Patient Service Microservice

A comprehensive Patient Management microservice built with Spring Boot for healthcare management systems.

## Features

- Complete CRUD operations for patient management
- Patient search and filtering
- Status management (Active/Inactive/Deceased)
- Emergency contact management
- Allergy tracking
- Service discovery with Eureka
- RESTful API endpoints
- Database persistence with PostgreSQL
- Docker containerization
- Health monitoring with Actuator
- Prometheus metrics

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud** (Eureka Client)
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

## Project Structure

```
patient-service/
├── src/
│   ├── main/
│   │   ├── java/com/healthcare/patientservice/
│   │   │   ├── controller/
│   │   │   │   └── PatientController.java
│   │   │   ├── service/
│   │   │   │   └── PatientService.java
│   │   │   ├── repository/
│   │   │   │   └── PatientRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── Patient.java
│   │   │   │   └── EmergencyContact.java
│   │   │   ├── dto/
│   │   │   │   └── PatientDTO.java
│   │   │   ├── mapper/
│   │   │   │   └── PatientMapper.java
│   │   │   ├── exception/
│   │   │   │   ├── PatientNotFoundException.java
│   │   │   │   ├── PatientAlreadyExistsException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ErrorResponse.java
│   │   │   └── PatientServiceApplication.java
│   │   └── resources/
│   │       └── application.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (optional)
- PostgreSQL 15+ (if running locally without Docker)

## Getting Started

### Running with Docker Compose (Recommended)

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Eureka Server on port 8761
- Patient Service on port 8081

### Running Locally

1. **Start PostgreSQL** and create database:
```sql
CREATE DATABASE patient_db;
```

2. **Build the project**:
```bash
mvn clean install
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

## API Endpoints

### Base URL
```
http://localhost:8081/patient-service/api/v1/patients
```

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create a new patient |
| GET | `/{id}` | Get patient by ID |
| GET | `/email/{email}` | Get patient by email |
| GET | `/` | Get all patients (paginated) |
| GET | `/search?query={term}` | Search patients |
| GET | `/status/{status}` | Get patients by status |
| PUT | `/{id}` | Update patient |
| DELETE | `/{id}` | Delete patient |
| PATCH | `/{id}/deactivate` | Deactivate patient |

### Sample Request - Create Patient

```json
POST /api/v1/patients
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-05-15",
  "gender": "MALE",
  "bloodGroup": "O+",
  "allergies": ["Penicillin", "Peanuts"],
  "medicalNotes": "Patient has history of asthma",
  "address": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "emergencyContacts": [
    {
      "name": "Jane Doe",
      "relationship": "Spouse",
      "phoneNumber": "+1234567891",
      "email": "jane.doe@example.com"
    }
  ]
}
```

### Sample Response

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-05-15",
  "gender": "MALE",
  "bloodGroup": "O+",
  "allergies": ["Penicillin", "Peanuts"],
  "medicalNotes": "Patient has history of asthma",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

## Pagination & Sorting

Use query parameters for pagination:
```
GET /api/v1/patients?page=0&size=10&sort=lastName,asc
```

## Health Check

```
GET http://localhost:8081/patient-service/actuator/health
```

## Metrics

```
GET http://localhost:8081/patient-service/actuator/metrics
GET http://localhost:8081/patient-service/actuator/prometheus
```

## Service Discovery

The service registers with Eureka Server at:
```
http://localhost:8761
```

## Testing with cURL

```bash
# Create a patient
curl -X POST http://localhost:8081/patient-service/api/v1/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE"
  }'

# Get patient by ID
curl http://localhost:8081/patient-service/api/v1/patients/1

# Search patients
curl http://localhost:8081/patient-service/api/v1/patients/search?query=john
```

## Future Enhancements

- JWT Authentication & Authorization
- API Gateway integration
- Event-driven communication with Kafka
- Redis caching
- Unit and integration tests
- API documentation with Swagger/OpenAPI
- File upload for medical documents
- Audit logging

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.