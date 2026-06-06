dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":auth:domain"))
    implementation(project(":auth:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
