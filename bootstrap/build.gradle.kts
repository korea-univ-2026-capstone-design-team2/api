plugins {
    id("org.springframework.boot")
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    // Question
    implementation(project(":question:application"))
    implementation(project(":question:adapter:web"))
    implementation(project(":question:adapter:persistence"))

    // Question Generation
    implementation(project(":question_generation:application"))
    implementation(project(":question_generation:adapter:web"))
    implementation(project(":question_generation:adapter:ai"))
    implementation(project(":question_generation:adapter:domain_connector"))
    implementation(project(":question_generation:adapter:persistence"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Spring AI
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai")
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai-embedding")
    implementation("org.springframework.ai:spring-ai-starter-vector-store-qdrant")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    // OAuth2
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")

    // Database
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
