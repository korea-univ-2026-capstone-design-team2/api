dependencies {
    implementation(project(":shared:kernel"))

    implementation(project(":exam:domain"))
    implementation(project(":exam:port"))

    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
