# Build stage
FROM amazoncorretto:17-alpine AS builder

WORKDIR /build

# Install required packages
RUN apk add --no-cache \
    dos2unix \
    maven

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy source code
COPY src src/

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine

WORKDIR /app

# Add non-root user
RUN addgroup -S spring && \
    adduser -S spring -G spring

# Copy the built artifact from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set ownership to spring user
RUN chown spring:spring app.jar

# Use spring as non-root user
USER spring

# Configure JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Set health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# Run the application with proper memory settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
