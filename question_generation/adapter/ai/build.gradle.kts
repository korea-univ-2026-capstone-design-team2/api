dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":question_generation:domain"))
    implementation(project(":question_generation:port"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Ai
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai")
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai-embedding")

    // Protobuf
    implementation("com.google.protobuf:protobuf-java:4.34.1")
}
