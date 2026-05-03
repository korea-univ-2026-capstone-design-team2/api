plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Spring Ai
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-common:3.0.3")

    // Configuration Processor
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
}
