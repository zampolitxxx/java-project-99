FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.4

ENV PORT=8080

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

RUN ./gradlew --no-daemon dependencies

COPY config config
COPY src src

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE ${PORT}

CMD java -Dspring.profiles.active=prod -jar build/libs/app-0.0.1-SNAPSHOT.jar
