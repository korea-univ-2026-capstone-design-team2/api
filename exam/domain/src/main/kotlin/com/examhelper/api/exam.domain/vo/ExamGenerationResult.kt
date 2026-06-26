package com.examhelper.api.exam.domain.vo

import com.examhelper.api.kernel.identifier.QuestionGenerationId

data class ExamGenerationResult(
    val generationId: QuestionGenerationId?,
    val successCount: Int?,
    val failCount: Int?
) {
    fun isPending(): Boolean = successCount == null
}
