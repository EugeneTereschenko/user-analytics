# User Analytics Java

A Spring Boot application for user analytics, featuring PostgreSQL, JPA, Liquibase, JWT authentication, and activity logging.

## Features

- RESTful API with Spring Boot
- PostgreSQL database integration
- JPA/Hibernate for ORM
- Liquibase for database migrations
- JWT-based authentication and Spring Security
- Activity logging (async supported)
- Testcontainers for integration testing

## Requirements

- Java 21+
- Maven 3.8+
- PostgreSQL 13+
- Node.js & npm (for Angular frontend, if present)

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd <project-root>
```

### 2. Configure the Database

Ensure PostgreSQL is running and create the database:

```bash
createdb analytics_db
```

Update `application.yml` with your database credentials if needed:
```

### 3. Build and Run

```bash
cd demo
mvn clean spring-boot:run
```

The server will start at [http://localhost:8080/](http://localhost:8080/).

### 4. Database Migrations

Liquibase will automatically apply migrations from `classpath:db/changelog/db.changelog-master.yaml` on startup.

### 5. Configuration

Key settings in `application.yml`:

- **Server port:** `8080`
- **Context path:** `/`
- **JPA:** Hibernate dialect, batch size, SQL formatting, etc.
- **Liquibase:** Enabled, schema set to `public`
- **Logging:** Custom levels for Liquibase, Hibernate, and application packages
- **Activity Logging:** Enabled and asynchronous

### 6. Testing

Run tests with:

```bash
mvn test
```

Testcontainers are used for integration tests with PostgreSQL.

## Project Structure

- `demo/` - Spring Boot backend
- `pom.xml` - Maven configuration

## Dependencies

- Spring Boot (Web, Data JPA, Security, Actuator)
- PostgreSQL JDBC Driver
- Liquibase
- JWT (io.jsonwebtoken)
- Lombok
- Testcontainers, AssertJ (for testing)

## License

See `LICENSE` file (if present).

---

**Note:** For Angular frontend setup, see the `frontend/` directory (if available).
