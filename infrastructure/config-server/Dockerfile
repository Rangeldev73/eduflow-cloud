FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -pl infrastructure/config-server -am -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/infrastructure/config-server/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]