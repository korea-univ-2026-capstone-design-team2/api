package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.type.QuestionItemStatus

data class QuestionItemDetailView(
    val questionItemId: Long,
    val questionId: Long,
    val generationId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val status: QuestionItemStatus,
    val qualityScore: Double?,
    val stem: String,
    val exhibitType: String?,
    val exhibitContent: String?,
    val propositions: List<QuestionItemPropositionView>?,
    val answerSheetType: String,
    val correctNumber: Int,
    val choices: List<AnswerChoiceViewWithAnswer>,
    val correctReason: String,
    val incorrectReasons: Map<String, String>,
)
