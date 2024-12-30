FROM maven:3.9.9-eclipse-temurin-17-alpine as builder
WORKDIR /opt/application
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17.0.13_11-jre-ubi9-minimal
WORKDIR /opt/application
EXPOSE 8080
COPY --from=builder /opt/application/target/*.jar /opt/application/app.jar
ENTRYPOINT ["java", "-jar", "/opt/application/app.jar"]