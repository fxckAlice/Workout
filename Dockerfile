FROM gradle:8.5.0-jdk21 AS build

WORKDIR /home/app

COPY . .

RUN gradle build -x test

FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /home/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
