# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and project files
COPY gradle /app/gradle
COPY gradlew build.gradle settings.gradle /app/

# Copy the source code
COPY src /app/src

# Grant execute permission to Gradle wrapper
RUN chmod +x gradlew

# Build the application using the Gradle wrapper
RUN ./gradlew build --no-daemon

# Set the JAR file location
ARG JAR_FILE=build/libs/*.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]