FROM maven:3.9.1-eclipse-temurin-17 AS build

WORKDIR /java-app

COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk

WORKDIR /java-app

COPY --from=build /java-app/target/reminder-0.0.1-SNAPSHOT.jar java-app.jar

EXPOSE 8080

CMD ["java", "-jar", "java-app.jar"]
