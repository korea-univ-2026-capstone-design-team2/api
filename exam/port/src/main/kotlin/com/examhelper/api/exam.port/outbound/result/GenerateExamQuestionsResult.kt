package com.examhelper.api.exam.port.outbound.result

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId

data class GenerateExamQuestionsResult(
    val generationId: QuestionGenerationId,
    val questionIds: List<QuestionId>,
    val successCount: Int,
    val failCount: Int
)
