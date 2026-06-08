package com.examhelper.api.auth.port.inbound.result

// [Result] 서비스가 일을 마치고 돌려주는 결과물
data class OAuthLoginResult(
    val accessToken: String,
    val refreshToken: String
)
