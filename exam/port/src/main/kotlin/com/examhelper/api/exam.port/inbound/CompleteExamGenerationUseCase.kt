package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.command.CompleteExamGenerationCommand

interface CompleteExamGenerationUseCase {
    fun execute(command: CompleteExamGenerationCommand)
}
