package com.examhelper.api.question_generation.port.inbound.result

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStatus

data class GenerateQuestionResult(
    val questionGenerationId: QuestionGenerationId,
    val questionIds: List<QuestionId>,
    val successCount: Int,
    val failCount: Int,
    val status: QuestionGenerationStatus
)
