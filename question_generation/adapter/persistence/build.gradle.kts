dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":question_generation:domain"))
    implementation(project(":question_generation:port"))

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.data:spring-data-jpa")
}
