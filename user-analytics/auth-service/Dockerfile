# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent POM (if needed)
COPY pom.xml ./parent-pom.xml

# Install parent POM (if needed)
RUN mvn install:install-file \
    -Dfile=parent-pom.xml \
    -DgroupId=com.example \
    -DartifactId=user-analytics \
    -Dversion=1.0.0 \
    -Dpackaging=pom \
    -DgeneratePom=false

# Copy billingservice module files
COPY auth-service/pom.xml ./pom.xml
COPY auth-service/src ./src

# Download dependencies
RUN mvn dependency:go-offline -B

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/auth-service-1.0.0.jar app.jar

EXPOSE 8087

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8087/auth-service/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
