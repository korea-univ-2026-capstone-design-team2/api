package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.port.inbound.view.QuestionPaperView

data class QuestionPaperResDto(
    val questionId: String,
    val generationId: String,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val sharedContextContent: String?,
    val sharedContextDescription: String?,
    val items: List<QuestionItemPaperResDto>,
) {
    companion object {
        fun fromView(view: QuestionPaperView): QuestionPaperResDto =
            QuestionPaperResDto(
                questionId = view.questionId.toString(),
                generationId = view.generationId.toString(),
                subject = view.subject,
                questionType = view.questionType,
                difficulty = view.difficulty,
                sharedContextContent = view.sharedContextContent,
                sharedContextDescription = view.sharedContextDescription,
                items = view.items.map(QuestionItemPaperResDto::fromView)
            )
    }
}
