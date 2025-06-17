FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY . .
RUN ./gradlew :impl:bootJar --no-daemon --no-watch-fs

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/impl/build/libs/impl-0.0.1-SNAPSHOT.jar ./impl-0.0.1-SNAPSHOT.jar

COPY test-container /app/test-container

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "impl-0.0.1-SNAPSHOT.jar"]
