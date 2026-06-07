package com.examhelper.api.auth.port.outbound

// 서비스가 사용할 규격화된 유저 프로필
data class OAuthUserProfile(
    val providerId: String, // 구글의 고유 번호 (sub)
    val email: String,
    val name: String?
)
