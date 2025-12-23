# Use Eclipse Temurin OpenJDK 11 as base image (official replacement for openjdk)
FROM eclipse-temurin:11-jre

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/pet-clinic-1.0.0.jar app.jar

# Expose port 8081 (application runs on 8081)
EXPOSE 8081

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/pet-clinic/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
