package com.examhelper.api.auth.adapter.web.response

import com.examhelper.api.auth.port.inbound.result.OAuthLoginResult

data class TokenResDto(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        // 비즈니스 처리 결과(Result)를 웹 응답(DTO)으로 변환합니다.
        fun from(result: OAuthLoginResult) = TokenResDto(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }
}