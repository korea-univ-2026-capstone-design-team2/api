package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionPaperView(
    val questionId: Long,
    val generationId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val sharedContextContent: String?,
    val sharedContextDescription: String?,
    val items: List<QuestionItemPaperView>,
)
