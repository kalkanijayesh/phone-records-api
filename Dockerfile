FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/phone-records-api-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
