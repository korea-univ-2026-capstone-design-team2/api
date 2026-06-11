package com.examhelper.api.member.adapter.web.response

import com.examhelper.api.member.port.inbound.result.MemberProfileResult

data class MemberProfileResDto(
    val id: Long,
    val nickname: String,
    val email: String,
    val authType: String
) {
    companion object {
        fun from(result: MemberProfileResult) = MemberProfileResDto(
            id = result.id,
            nickname = result.nickname,
            email = result.email,
            authType = result.authType
        )
    }
}
