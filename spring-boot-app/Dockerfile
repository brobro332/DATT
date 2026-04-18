FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

FROM amazoncorretto:17-al2023-headless
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/.env.local .env

ENTRYPOINT ["java", "-jar", "app.jar"]