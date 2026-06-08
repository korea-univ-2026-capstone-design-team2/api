package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.port.inbound.view.QuestionDetailView

data class QuestionDetailResDto(
    val questionId: String,
    val generationId: String,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val status: QuestionStatus,
    val passageTopicCategory: String?,
    val passageTopicKeyword: String?,
    val sharedContextContent: String?,
    val sharedContextDescription: String?,
    val items: List<QuestionItemDetailResDto>,
) {
    companion object {
        fun fromView(view: QuestionDetailView): QuestionDetailResDto =
            QuestionDetailResDto(
                questionId = view.questionId.toString(),
                generationId = view.generationId.toString(),
                subject = view.subject,
                questionType = view.questionType,
                difficulty = view.difficulty,
                status = view.status,
                passageTopicCategory = view.passageTopicCategory,
                passageTopicKeyword = view.passageTopicKeyword,
                sharedContextContent = view.sharedContextContent,
                sharedContextDescription = view.sharedContextDescription,
                items = view.items.map(QuestionItemDetailResDto::fromView)
            )
    }
}
