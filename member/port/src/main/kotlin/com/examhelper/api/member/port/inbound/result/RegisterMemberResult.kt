package com.examhelper.api.member.port.inbound.result

data class RegisterMemberResult(
    val memberId: Long,
    val email: String
)