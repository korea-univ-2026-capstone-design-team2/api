package com.examhelper.api.auth.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.identifier.SocialIdentityId
import java.time.Instant

enum class OAuthProvider {
    GOOGLE, KAKAO, APPLE
}

class SocialIdentity private constructor(
    id: SocialIdentityId,
    val memberId : MemberId, // 연결된 유저의 시스템 ID
    val provider: OAuthProvider,
    val providerId: String, // 고유 식별자
    val createdAt: Instant
): AggregateRoot<SocialIdentityId>(id) {
    // 팩토리 메서드
    companion object {
        // 처음 구글로 로그인해서 소셜 계정을 연동할 때 사용
        fun create(
            id: SocialIdentityId,
            memberId: MemberId,
            provider: OAuthProvider,
            providerId: String
        ): SocialIdentity {
            return SocialIdentity(
                id = id,
                memberId = memberId,
                provider = provider,
                providerId = providerId,
                createdAt = Instant.now()
            )
        }

        // DB에서 기존 연동 정보를 불러올 때 사용
        fun of(
            id: SocialIdentityId,
            memberId: MemberId,
            provider: OAuthProvider,
            providerId: String,
            createdAt: Instant
        ): SocialIdentity {
            return SocialIdentity(
                id, memberId, provider, providerId, createdAt
            )
        }
    }
}