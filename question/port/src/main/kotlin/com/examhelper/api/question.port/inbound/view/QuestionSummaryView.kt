package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionStatus

data class QuestionSummaryView(
    val questionId: Long,
    val questionSetId: Long?,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val status: QuestionStatus,
    val qualityScore: Double?,
)
