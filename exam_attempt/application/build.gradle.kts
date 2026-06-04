dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":exam_attempt:domain"))
    implementation(project(":exam_attempt:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
