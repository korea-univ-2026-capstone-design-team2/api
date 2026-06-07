package com.examhelper.api.auth.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.CredentialId
import com.examhelper.api.kernel.identifier.MemberId
import java.time.Instant

enum class OAuthProvider {
    GOOGLE, KAKAO, APPLE
}

class OAuthCredential private constructor(
    id: CredentialId,             // SocialIdentityId 대신 CredentialId 사용
    val memberId: MemberId,       // 연결된 유저의 시스템 ID
    val provider: OAuthProvider,
    val providerId: String,       // 고유 식별자
    val email: String,        // 🟢 누락되었던 이메일 추가
    val linkedAt: Instant     // 🟢 createdAt에서 linkedAt으로 네이밍 변경
) : AggregateRoot<CredentialId>(id) {

    // 팩토리 메서드
    companion object {
        // 처음 구글로 로그인해서 소셜 계정을 연동할 때 사용
        fun create(
            id: CredentialId,
            memberId: MemberId,
            provider: OAuthProvider,
            providerId: String,
            email: String         // 🟢 파라미터 추가
        ): OAuthCredential {
            return OAuthCredential(
                id = id,
                memberId = memberId,
                provider = provider,
                providerId = providerId,
                email = email,
                linkedAt = Instant.now()
            )
        }

        // DB에서 기존 연동 정보를 불러올 때 사용
        fun of(
            id: CredentialId,
            memberId: MemberId,
            provider: OAuthProvider,
            providerId: String,
            email: String,        // 🟢 파라미터 추가
            linkedAt: Instant
        ): OAuthCredential {
            return OAuthCredential(
                id = id,
                memberId = memberId,
                provider = provider,
                providerId = providerId,
                email = email,
                linkedAt = linkedAt
            )
        }
    }
}