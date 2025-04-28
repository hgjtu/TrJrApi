FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

COPY . .

RUN ./gradlew clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8010

ENTRYPOINT ["java", "-jar", "app.jar"]