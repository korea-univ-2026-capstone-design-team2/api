package com.examhelper.api.auth.port.outbound

import com.examhelper.api.auth.domain.OAuthCredential
import com.examhelper.api.auth.domain.OAuthProvider

interface OAuthCredentialStore {
    fun save(credential: OAuthCredential): OAuthCredential
    fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): OAuthCredential?
}