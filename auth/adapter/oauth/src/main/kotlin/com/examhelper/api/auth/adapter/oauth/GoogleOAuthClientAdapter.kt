package com.examhelper.api.auth.adapter.oauth

import com.examhelper.api.auth.domain.OAuthProvider
import com.examhelper.api.auth.port.outbound.OAuthClientPort
import com.examhelper.api.auth.port.outbound.OAuthUserProfile
import com.google.auth.oauth2.TokenVerifier // 🟢 새로운 라이브러리의 핵심 클래스
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Value

@Repository
class GoogleOAuthClientAdapter(
    // application.yml에서 클라이언트 ID 주입
    @Value("\${oauth.google.client-id}") private val googleClientId: String
) : OAuthClientPort {

    // 💡 최신 TokenVerifier 세팅 (복잡한 팩토리 설정 없이 타겟 대상만 명시하면 됩니다)
    private val verifier = TokenVerifier.newBuilder()
        .setAudience(googleClientId)
        .build()

    override fun getUserProfile(provider: OAuthProvider, idToken: String): OAuthUserProfile {

        if (provider != OAuthProvider.GOOGLE) {
            throw IllegalArgumentException("Unsupported provider: $provider")
        }

        return try {
            // 1. 토큰 검증 (서명, 만료시간, 대상자 일치 여부를 모두 자동으로 검사)
            val jsonWebSignature = verifier.verify(idToken)

            // 2. 알맹이(Payload) 꺼내기
            val payload = jsonWebSignature.payload

            // 3. 우리 서비스 도메인 객체로 변환하여 반환
            // (payload는 Map처럼 작동하므로 ["키"] 형태로 값을 꺼낼 수 있습니다)
            OAuthUserProfile(
                providerId = payload.subject, // 구글의 고유 식별자 (sub)
                email = payload["email"] as String,
                name = payload["name"] as? String
            )

        } catch (e: TokenVerifier.VerificationException) {
            // 🚨 토큰이 만료되었거나, 위조되었거나, 클라이언트 ID가 다르면 여기서 에러가 터집니다.
            throw IllegalArgumentException("Invalid or expired Google ID token", e)
        }
    }
}