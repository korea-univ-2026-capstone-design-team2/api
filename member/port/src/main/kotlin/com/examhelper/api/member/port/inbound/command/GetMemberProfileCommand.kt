package com.examhelper.api.member.port.inbound.command

data class GetMemberProfileCommand(
    val memberId: Long // 토큰에서 추출한 유저 ID
)