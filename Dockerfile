#
#FROM openjdk:11-jdk-slim
#WORKDIR /app
#COPY target/ErgonomicCalculator.jar /app/ErgonomicCalculator.jar
#COPY src/main/resources /app/resources
#EXPOSE 8081
#CMD ["java", "-jar", "ErgonomicCalculator.jar"]

# Use Maven with JDK 11 for the build stage
FROM maven:3.8.5-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven POM file and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Use a smaller base image for the runtime environment
FROM openjdk:11-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/ErgonomicCalculator.jar /app/ErgonomicCalculator.jar

# Expose the port that the application will run on
EXPOSE 8081

# Specify the command to run the JAR file
CMD ["java", "-jar", "ErgonomicCalculator.jar"]
