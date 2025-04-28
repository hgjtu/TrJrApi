FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8010

ENTRYPOINT ["java", "-jar", "app.jar"]