FROM openjdk:17-slim

RUN apt-get update && apt-get install -y \
    curl \
    procps \
    time \
    wget \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

ARG JUNIT_VERSION=1.9.3
RUN wget -O junit-platform-console-standalone.jar \
    https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar

COPY run-tests.sh /app/run-tests.sh
RUN chmod +x /app/run-tests.sh

ENTRYPOINT ["/app/run-tests.sh"]
