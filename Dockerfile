# ─────────────────────────────────────────────────────────────────────────────
# Stage 1 · Gradle 설정 파일만 추출
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-noble AS gradle-specs

WORKDIR /workspace

# 전체 소스 복사 후 build.gradle.kts / settings.gradle.kts 외 전부 삭제
COPY . .
RUN find . -type f \
      ! -name "build.gradle.kts" \
      ! -name "settings.gradle.kts" \
      ! -path "*/gradle/*" \
      ! -name "gradlew" \
      -delete \
    && find . -type d -empty -delete

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2 · Build
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-noble AS builder

WORKDIR /workspace

# 1) Gradle wrapper
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

# 2) 설정 파일만 추출된 Stage에서 복사 → 모듈 나열 불필요
COPY --from=gradle-specs /workspace .

# 3) 의존성 resolve
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon -q 2>/dev/null || true

# 4) 전체 소스 복사 후 빌드
COPY . .
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :bootstrap:bootJar --no-daemon -x test

# ─────────────────────────────────────────────────────────────────────────────
# Stage 3 · Runtime
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-noble AS runtime

RUN groupadd --system app && useradd --system --gid app app

WORKDIR /app

COPY --from=builder /workspace/bootstrap/build/libs/*.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
