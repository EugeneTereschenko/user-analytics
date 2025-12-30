# Eureka Server

This is the Eureka Server for the Healthcare microservices system. It provides service discovery for all registered microservices.

## Features

- Service registry and discovery using Netflix Eureka
- Spring Boot based application

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker (optional, for containerized deployment)

## Build

To build the application:

```bash
mvn clean package
```

Run (Locally)
To start the Eureka Server locally:
```bash
mvn spring-boot:run
```

or 
```bash
java -jar target/eureka-server-*.jar
```

The server will start on http://localhost:8761.
##
Run (Docker)
To build and run with Docker Compose:

```bash
make eurekaserver
make eurekaserver-up
```

Usage
Access the Eureka dashboard at http://localhost:8761
##
Other microservices will register themselves automatically
##
License
© 2025 Yevhen Tereshchenko. All rights reserved.