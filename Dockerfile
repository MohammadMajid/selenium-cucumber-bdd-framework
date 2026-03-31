FROM maven:3.9.11-eclipse-temurin-17

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl jq \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

CMD ["mvn", "-version"]