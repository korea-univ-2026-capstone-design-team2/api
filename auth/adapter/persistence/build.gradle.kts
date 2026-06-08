plugins {
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":auth:domain"))
    implementation(project(":auth:port"))

    implementation(project(":member:domain"))

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.data:spring-data-jpa")
}
