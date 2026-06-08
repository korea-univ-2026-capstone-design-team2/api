package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.port.inbound.view.QuestionReviewView

data class QuestionReviewResDto(
    val questionId: String,
    val generationId: String,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val sharedContextContent: String?,
    val sharedContextDescription: String?,
    val items: List<QuestionItemReviewResDto>,
) {
    companion object {
        fun fromView(view: QuestionReviewView): QuestionReviewResDto =
            QuestionReviewResDto(
                questionId = view.questionId.toString(),
                generationId = view.generationId.toString(),
                subject = view.subject,
                questionType = view.questionType,
                difficulty = view.difficulty,
                sharedContextContent = view.sharedContextContent,
                sharedContextDescription = view.sharedContextDescription,
                items = view.items.map(QuestionItemReviewResDto::fromView)
            )
    }
}
