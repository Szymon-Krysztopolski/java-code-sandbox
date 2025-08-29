# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Runtime stage
FROM eclipse-temurin:21-alpine
COPY --from=build target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
