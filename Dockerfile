FROM maven:3.9.11-eclipse-temurin-8 AS builder
LABEL authors="karthikdasari"
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jre
WORKDIR /app
RUN adduser --system appuser

COPY --from=builder /app/target/*.jar app.jar
USER appuser

EXPOSE 8000


CMD ["java", "-jar", "app.jar"]