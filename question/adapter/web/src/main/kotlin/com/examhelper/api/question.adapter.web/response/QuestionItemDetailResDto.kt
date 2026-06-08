package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionItemStatus
import com.examhelper.api.question.port.inbound.view.QuestionItemDetailView

data class QuestionItemDetailResDto(
    val questionItemId: String,
    val questionId: String,
    val generationId: String,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val status: QuestionItemStatus,
    val qualityScore: Double?,
    val stem: String,
    val exhibitType: String?,
    val exhibitContent: String?,
    val propositions: List<QuestionItemPropositionResDto>?,
    val answerSheetType: String,
    val correctNumber: Int,
    val choices: List<AnswerChoiceWithAnswerResDto>,
    val correctReason: String,
    val incorrectReasons: Map<String, String>,
) {
    companion object {
        fun fromView(view: QuestionItemDetailView): QuestionItemDetailResDto =
            QuestionItemDetailResDto(
                questionItemId = view.questionItemId.toString(),
                questionId = view.questionId.toString(),
                generationId = view.generationId.toString(),
                subject = view.subject,
                questionType = view.questionType,
                questionSubType = view.questionSubType,
                difficulty = view.difficulty,
                status = view.status,
                qualityScore = view.qualityScore,
                stem = view.stem,
                exhibitType = view.exhibitType,
                exhibitContent = view.exhibitContent,
                propositions = view.propositions?.map(QuestionItemPropositionResDto::fromView),
                answerSheetType = view.answerSheetType,
                correctNumber = view.correctNumber,
                choices = view.choices.map(AnswerChoiceWithAnswerResDto::fromView),
                correctReason = view.correctReason,
                incorrectReasons = view.incorrectReasons
            )
    }
}
