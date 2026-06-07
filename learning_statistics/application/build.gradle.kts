dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":learning_statistics:domain"))
    implementation(project(":learning_statistics:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
