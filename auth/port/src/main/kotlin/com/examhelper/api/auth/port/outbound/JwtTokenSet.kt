package com.examhelper.api.auth.port.outbound

data class JwtTokenSet(
    val accessToken: String,
    val refreshToken: String
)
