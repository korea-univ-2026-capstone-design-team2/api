dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":exam:domain"))
    implementation(project(":exam:port"))

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
}
