package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionStatus

data class QuestionDetailView(
    val questionId: Long,
    val generationId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val status: QuestionStatus,
    val passageTopicCategory: String?,
    val passageTopicKeyword: String?,
    val sharedContextContent: String?,
    val sharedContextDescription: String?,
    val items: List<QuestionItemDetailView>,
)
