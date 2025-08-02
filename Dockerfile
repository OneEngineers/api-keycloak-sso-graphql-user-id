# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy build files
COPY . .

RUN ./mvnw clean package -DskipTests

# RUN ./gradlew clean bootJar

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
