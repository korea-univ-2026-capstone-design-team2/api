package com.examhelper.api.member.port.inbound.result

data class MemberProfileResult(
    val id: Long,
    val nickname: String,
    val email: String,
    val authType: String
)
