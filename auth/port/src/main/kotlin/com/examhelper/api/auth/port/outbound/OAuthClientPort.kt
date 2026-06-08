package com.examhelper.api.auth.port.outbound

import com.examhelper.api.auth.domain.OAuthProvider

interface OAuthClientPort {
    // 특정 소셜(Google 등)의 토큰을 검증하고 프로필을 반환합니다.
    fun getUserProfile(provider: OAuthProvider, idToken: String): OAuthUserProfile
}