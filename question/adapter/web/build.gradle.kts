dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":question:port"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
}
