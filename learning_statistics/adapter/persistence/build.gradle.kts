plugins {
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":learning_statistics:domain"))
    implementation(project(":learning_statistics:port"))

    // Database
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.data:spring-data-jpa")
}
