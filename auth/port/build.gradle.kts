dependencies {
    implementation(project(":shared:kernel"))
    implementation(project(":auth:domain"))
    // JWT (JSON Web Token) 라이브러리 추가
    // 1. JWT API (인터페이스)
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")

    // 2. JWT 구현체 (런타임 시 동작)
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")

    // 3. JSON 직렬화/역직렬화를 위한 Jackson 지원
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
}
