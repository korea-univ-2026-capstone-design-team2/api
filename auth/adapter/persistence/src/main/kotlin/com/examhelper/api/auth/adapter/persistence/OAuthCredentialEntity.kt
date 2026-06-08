package com.examhelper.api.auth.adapter.persistence

import com.examhelper.api.auth.domain.OAuthCredential
import com.examhelper.api.auth.domain.OAuthProvider
import com.examhelper.api.kernel.identifier.CredentialId
import com.examhelper.api.kernel.identifier.MemberId
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "oauth_credentials")
class OAuthCredentialEntity(
    @Id
    val id: Long,

    @Column(nullable = false)
    val memberId: Long, // 연결된 회원 번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: OAuthProvider, // GOOGLE, KAKAO 등

    @Column(nullable = false, unique = true)
    val providerId: String, // 구글이 준 고유 번호 (sub)

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, updatable = false)
    val linkedAt: Instant
) {
    companion object {
        fun fromDomain(domain: OAuthCredential): OAuthCredentialEntity = OAuthCredentialEntity(
            id = domain.id.value,
            memberId = domain.memberId.value,
            provider = domain.provider,
            providerId = domain.providerId,
            email = domain.email,
            linkedAt = domain.linkedAt
        )
    }

    fun toDomain(): OAuthCredential = OAuthCredential.of(
        id = CredentialId(id),
        memberId = MemberId(memberId),
        provider = provider,
        providerId = providerId,
        email = email,
        linkedAt = linkedAt
    )
}