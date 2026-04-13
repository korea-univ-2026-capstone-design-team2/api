plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")

    // Configuration Processor
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
}
