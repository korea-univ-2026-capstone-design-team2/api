package com.examhelper.api.member.port.inbound.command

data class RegisterMemberCommand(
    val email: String,
    val name: String
)