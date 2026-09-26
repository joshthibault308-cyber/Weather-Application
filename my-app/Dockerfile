FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline

COPY ./src src/

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package && \
    cp target/*.jar target/app.jar

FROM eclipse-temurin:21-jre-jammy

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

USER appuser
WORKDIR /app

COPY --from=builder --chown=appuser:appuser /build/target/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]