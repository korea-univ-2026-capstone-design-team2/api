package com.examhelper.api.auth.adapter.web.request

import com.examhelper.api.auth.domain.OAuthProvider
import com.examhelper.api.auth.port.inbound.command.OAuthLoginCommand

// [요청 DTO] 프론트엔드가 보내주는 데이터
data class OAuthLoginReqDto(
    val provider: String, // 예: "GOOGLE", "KAKAO"
    val idToken: String   // 구글 SDK 등을 통해 받아온 인증 토큰
) {
    // 💡 웹 DTO를 비즈니스 계층이 이해할 수 있는 Command로 변환합니다.
    fun toCommand() = OAuthLoginCommand(
        provider = OAuthProvider.valueOf(provider.uppercase()),
        idToken = idToken
    )
}
