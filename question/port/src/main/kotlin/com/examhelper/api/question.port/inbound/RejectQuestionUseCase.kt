package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.command.RejectQuestionCommand
import com.examhelper.api.question.port.inbound.result.RejectQuestionResult

interface RejectQuestionUseCase {
    fun execute(command: RejectQuestionCommand): RejectQuestionResult
}
