package com.examhelper.api.member.adapter.web.response

import com.examhelper.api.member.port.inbound.result.RegisterMemberResult

data class RegisterMemberResDto(
    val memberId: Long,
    val email: String
) {
    companion object {
        // 도메인 결과를 웹 응답 객체로 변환
        fun fromResult(result: RegisterMemberResult): RegisterMemberResDto {
            return RegisterMemberResDto(
                memberId = result.memberId,
                email = result.email
            )
        }
    }
}