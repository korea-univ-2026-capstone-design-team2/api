package com.examhelper.api.auth.adapter.persistence

import com.examhelper.api.auth.domain.OAuthCredential
import com.examhelper.api.auth.domain.OAuthProvider
import com.examhelper.api.auth.port.outbound.OAuthCredentialStore
import org.springframework.stereotype.Repository

@Repository
class OAuthCredentialPersistenceAdapter(
    private val jpaRepository: OAuthCredentialJpaRepository
) : OAuthCredentialStore {

    override fun save(credential: OAuthCredential): OAuthCredential {
        val entity = OAuthCredentialEntity.fromDomain(credential)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): OAuthCredential? {
        val entity = jpaRepository.findByProviderAndProviderId(provider, providerId)
        return entity?.toDomain() // entity가 null이면 null 반환, 아니면 도메인으로 변환
    }
}