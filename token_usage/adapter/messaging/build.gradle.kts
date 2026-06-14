dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":token_usage:domain"))
    implementation(project(":token_usage:port"))

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
}
