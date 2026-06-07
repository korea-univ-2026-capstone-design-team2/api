package com.examhelper.api.exam_attempt.port.outbound.result

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionSummaryResult(
    val questionItemId: Long,
    val generationId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel
)
