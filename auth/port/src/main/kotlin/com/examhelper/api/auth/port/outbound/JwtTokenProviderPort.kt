package com.examhelper.api.auth.port.outbound

import com.examhelper.api.kernel.identifier.MemberId

interface JwtTokenProviderPort {
    fun generateTokens(memberId: MemberId): JwtTokenSet
    fun validateAndExtractMemberId(token: String): MemberId?
}