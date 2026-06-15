dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":question_generation:domain"))
    implementation(project(":question_generation:port"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-core")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-common:3.0.3")
}
