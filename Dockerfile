FROM maven:3.9.4-eclipse-temurin-17 AS build

LABEL maintainer="stepan.morgachev@gmail.com"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

ENV JAR_FILE=Senla-project-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY --from=build /app/target/${JAR_FILE} /app/${JAR_FILE}

ENTRYPOINT ["java", "-jar", "/app/Senla-project-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
