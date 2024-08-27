FROM maven:3.9.1-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src /app/src

RUN mvn clean install

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app-1.0.0.jar

EXPOSE 8080

CMD ["java", "-jar", "app-1.0.0.jar"]
