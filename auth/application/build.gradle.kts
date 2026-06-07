dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":auth:domain"))
    implementation(project(":auth:port"))
    implementation(project(":member:domain"))
    implementation(project(":member:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
