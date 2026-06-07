package com.examhelper.api.question.adapter.persistence.projection

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionStatus

interface QuestionItemSummaryProjection {
    val questionItemId: Long
    val generationId: Long
    val subject: Subject
    val questionType: QuestionType
    val difficulty: DifficultyLevel
    val status: QuestionStatus
}
