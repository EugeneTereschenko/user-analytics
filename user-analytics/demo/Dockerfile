# Use Eclipse Temurin OpenJDK 21 as base image
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the built jar file into the container
COPY target/demo-*.jar app.jar

# Expose port 8082
EXPOSE 8082

# Set environment variables for Java
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=8082"]
