package com.examhelper.api.member.port.inbound

import com.examhelper.api.member.port.inbound.command.GetMemberProfileCommand
import com.examhelper.api.member.port.inbound.result.MemberProfileResult

interface GetMemberProfileUseCase {
    fun execute(command: GetMemberProfileCommand): MemberProfileResult
}