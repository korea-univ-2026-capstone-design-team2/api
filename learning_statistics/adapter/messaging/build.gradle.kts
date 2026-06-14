dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":learning_statistics:domain"))
    implementation(project(":learning_statistics:port"))

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
}
