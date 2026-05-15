plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-common:3.0.3")

    // Configuration Processor
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")

    // Test
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}
