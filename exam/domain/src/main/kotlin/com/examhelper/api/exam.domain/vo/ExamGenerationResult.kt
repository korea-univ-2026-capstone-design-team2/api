package com.examhelper.api.exam.domain.vo

import com.examhelper.api.kernel.identifier.QuestionGenerationId

data class ExamGenerationResult(
    val generationId: QuestionGenerationId?,
    val successCount: Int,
    val failCount: Int
) {
    val totalAttempted: Int get() = successCount + failCount

    fun hasPartialFailure(): Boolean = failCount > 0
}
