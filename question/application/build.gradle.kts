dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":question:domain"))
    implementation(project(":question:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
