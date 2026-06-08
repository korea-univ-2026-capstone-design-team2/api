package com.examhelper.api.auth.adapter.persistence

import com.examhelper.api.auth.domain.OAuthProvider
import org.springframework.data.jpa.repository.JpaRepository

interface OAuthCredentialJpaRepository : JpaRepository<OAuthCredentialEntity, Long> {
    // 💡 구글 로그인 시 "이 구글 ID로 가입한 적이 있나?" 찾을 때 쓰이는 핵심 쿼리메서드
    fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): OAuthCredentialEntity?
}