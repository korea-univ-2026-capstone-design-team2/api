package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionItemMetadataView(
    val questionItemId: Long,
    val generationId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel
)
