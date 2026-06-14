plugins {
    id("org.springframework.boot")
    kotlin("kapt")
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    // Question
    implementation(project(":question:application"))
    implementation(project(":question:adapter:web"))
    implementation(project(":question:adapter:persistence"))

    // Member
    implementation(project(":member:application"))
    implementation(project(":member:adapter:web"))
    implementation(project(":member:adapter:persistence"))

    // Question Generation
    implementation(project(":question_generation:application"))
    implementation(project(":question_generation:adapter:web"))
    implementation(project(":question_generation:adapter:ai"))
    implementation(project(":question_generation:adapter:domain_connector"))
    implementation(project(":question_generation:adapter:persistence"))

    // Exam
    implementation(project(":exam:application"))
    implementation(project(":exam:adapter:web"))
    implementation(project(":exam:adapter:persistence"))
    implementation(project(":exam:adapter:domain_connector"))
    implementation(project(":exam:adapter:messaging"))

    // Exam Attempt
    implementation(project(":exam_attempt:application"))
    implementation(project(":exam_attempt:adapter:web"))
    implementation(project(":exam_attempt:adapter:persistence"))
    implementation(project(":exam_attempt:adapter:domain_connector"))

    // Token Usage
    implementation(project(":token_usage:application"))
    implementation(project(":token_usage:adapter:web"))
    implementation(project(":token_usage:adapter:persistence"))
    implementation(project(":token_usage:adapter:messaging"))

    // Learning Statistics
    implementation(project(":learning_statistics:application"))
    implementation(project(":learning_statistics:adapter:web"))
    implementation(project(":learning_statistics:adapter:persistence"))
    implementation(project(":learning_statistics:adapter:messaging"))

    // Auth
    implementation(project(":auth:application"))
    implementation(project(":auth:adapter:web"))
    implementation(project(":auth:adapter:persistence"))
    implementation(project(":auth:adapter:oauth"))
    implementation(project(":auth:adapter:jwt"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Spring AI
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
    // implementation("org.springframework.ai:spring-ai-starter-model-google-genai")
    // implementation("org.springframework.ai:spring-ai-starter-model-google-genai-embedding")
    implementation("org.springframework.ai:spring-ai-starter-vector-store-qdrant")

    // Kafka
    implementation("org.springframework.boot:spring-boot-starter-kafka")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.23.0")

    // Database
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}
