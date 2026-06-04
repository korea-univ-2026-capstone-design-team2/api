package com.examhelper.api.member.adapter.web.request

import com.examhelper.api.member.port.inbound.command.RegisterMemberCommand

data class RegisterMemberReqDto(
    val email: String,
    val name: String
) {
    // 헥사고날의 핵심: 웹 DTO를 도메인 커맨드로 변환하는 메서드
    fun toCommand(): RegisterMemberCommand {
        return RegisterMemberCommand(
            email = this.email,
            name = this.name
        )
    }
}
