plugins {
    id("org.springframework.boot")
    kotlin("kapt")
    kotlin("plugin.jpa")
}

apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

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

    // Exam
    implementation(project(":exam:application"))
    implementation(project(":exam:adapter:web"))
    implementation(project(":exam:adapter:persistence"))
    implementation(project(":exam:adapter:domain_connector"))

    // Token Usage
    implementation(project(":token_usage:application"))
    implementation(project(":token_usage:adapter:web"))
    implementation(project(":token_usage:adapter:persistence"))

    // Auth
    implementation(project(":auth"))

    // Member
    implementation(project(":member"))

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

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

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
