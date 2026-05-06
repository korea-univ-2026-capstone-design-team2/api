plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // OAuth2
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.data:spring-data-jpa")
}
