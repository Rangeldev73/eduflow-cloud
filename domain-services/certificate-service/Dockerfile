FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -pl domain-services/certificate-service -am -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/domain-services/certificate-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]