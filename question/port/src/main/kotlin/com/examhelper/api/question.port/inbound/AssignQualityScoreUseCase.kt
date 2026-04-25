package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.command.AssignQualityScoreCommand
import com.examhelper.api.question.port.inbound.result.AssignQualityScoreResult

interface AssignQualityScoreUseCase {
    fun execute(command: AssignQualityScoreCommand): AssignQualityScoreResult
}
