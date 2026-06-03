dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":token_usage:domain"))
    implementation(project(":token_usage:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
