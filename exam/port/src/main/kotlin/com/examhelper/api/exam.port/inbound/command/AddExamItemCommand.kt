package com.examhelper.api.exam.port.inbound.command

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId

data class AddExamItemCommand(
    val generationId: QuestionGenerationId,
    val questionId: QuestionId,
    val ordering: Int
)
