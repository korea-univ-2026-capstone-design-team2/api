dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":question_generation:domain"))
    implementation(project(":question_generation:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
