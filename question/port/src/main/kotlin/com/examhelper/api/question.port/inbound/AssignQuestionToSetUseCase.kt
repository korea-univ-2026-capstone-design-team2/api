package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.command.AssignQuestionToSetCommand
import com.examhelper.api.question.port.inbound.result.AssignQuestionToSetResult

interface AssignQuestionToSetUseCase {
    fun execute(command: AssignQuestionToSetCommand): AssignQuestionToSetResult
}
