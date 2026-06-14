package com.examhelper.api.exam.port.inbound.command

import com.examhelper.api.kernel.identifier.QuestionGenerationId

data class CompleteExamGenerationCommand(
    val generationId: QuestionGenerationId,
    val successCount: Int,
    val failCount: Int
)
