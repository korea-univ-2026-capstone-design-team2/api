package com.examhelper.api.auth.port.inbound.command

import com.examhelper.api.auth.domain.OAuthProvider

// [Command] 서비스가 일하기 위해 필요한 재료
data class OAuthLoginCommand(
    val provider: OAuthProvider,
    val idToken: String
)