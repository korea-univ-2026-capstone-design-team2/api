package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.command.GenerateExamCommand
import com.examhelper.api.exam.port.inbound.result.GenerateExamResult

interface GenerateExamUseCase {
    fun execute(command: GenerateExamCommand): GenerateExamResult
}
