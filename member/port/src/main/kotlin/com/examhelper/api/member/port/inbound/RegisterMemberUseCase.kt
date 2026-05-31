package com.examhelper.api.member.port.inbound

import com.examhelper.api.member.port.inbound.command.RegisterMemberCommand
import com.examhelper.api.member.port.inbound.result.RegisterMemberResult

interface RegisterMemberUseCase {
    fun execute(command: RegisterMemberCommand): RegisterMemberResult
}