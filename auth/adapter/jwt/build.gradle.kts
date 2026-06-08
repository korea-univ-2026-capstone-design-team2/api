plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":shared:infrastructure"))

    implementation(project(":auth:domain"))
    implementation(project(":auth:port"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    // JWT (JSON Web Token) 라이브러리 추가
    // 1. JWT API (인터페이스)
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")

    // 2. JWT 구현체 (런타임 시 동작)
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")

    // 3. JSON 직렬화/역직렬화를 위한 Jackson 지원
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}
