dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":exam:domain"))
    implementation(project(":exam:port"))

    implementation(project(":question:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
