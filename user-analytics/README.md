# User Analytics

User Analytics is a Spring Boot-based microservice for collecting and analyzing user data. It is designed to be part of a larger microservices architecture.

## Features

- RESTful API for user analytics data
- SQL database integration
- Maven-based build
- Docker support

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (optional, for containerization)
- SQL database (e.g., PostgreSQL, MySQL)


## Using the Makefile

The provided `Makefile` simplifies common development and Docker Compose tasks. Key commands:

- `make up` – Start all services in detached mode
- `make down` – Stop all services
- `make build` – Build all services
- `make logs` – Show logs for all services
- `make restart` – Restart all services
- `make clean` – Remove all services, volumes, and unused data
- `make postgres` – Start only the postgres service

Service-specific commands (replace `<service>` with the actual name, e.g., `patientservice`, `eurekaserver`, `analytics-service`, `appointment-service`, `medicalrecordservice`):

- `make <service>` – Build only the service
- `make <service>-up` – Start only the service
- `make <service>-down` – Stop only the service

For a full list, run:


make help

## Build and Run

### Build with Maven

```bash
mvn clean package


