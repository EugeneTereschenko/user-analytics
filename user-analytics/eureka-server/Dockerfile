# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent POM (now relative to root context)
COPY pom.xml ./parent-pom.xml

# Install parent POM directly without project scanning
RUN mvn install:install-file \
    -Dfile=parent-pom.xml \
    -DgroupId=com.example \
    -DartifactId=user-analytics \
    -Dversion=1.0.0 \
    -Dpackaging=pom \
    -DgeneratePom=false


# Now copy eureka-server module
COPY eureka-server/pom.xml ./pom.xml
COPY eureka-server/src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/eureka-server-*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]
