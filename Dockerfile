# Use an official Maven image to build the project
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the project files and build the project
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
