FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/Senla-project-0.0.1-SNAPSHOT.jar /app/Senla-project-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/Senla-project-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
