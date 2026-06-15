# ─────────────────────────────────────────────────────────────
# Build Stage
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-noble AS builder

WORKDIR /workspace

# Gradle Wrapper
COPY gradlew .
COPY gradle gradle

RUN chmod +x gradlew

# 전체 소스 복사
COPY . .

# Build
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :bootstrap:bootJar \
    --no-daemon \
    -x test

# ─────────────────────────────────────────────────────────────
# Runtime Stage
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-noble AS runtime

RUN groupadd --system app \
 && useradd --system --gid app app

WORKDIR /app

COPY --from=builder /workspace/bootstrap/build/libs/*.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Xms128m", \
    "-Xmx512m", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
