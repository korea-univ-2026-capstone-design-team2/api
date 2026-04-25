package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.command.PublishQuestionCommand
import com.examhelper.api.question.port.inbound.result.PublishQuestionResult

interface PublishQuestionUseCase {
    fun execute(command: PublishQuestionCommand): PublishQuestionResult
}
