package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionItemPaperView(
    val questionItemId: Long,
    val questionId: Long,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val stem: String,
    val exhibitType: String?,
    val exhibitContent: String?,
    val propositions: List<QuestionItemPropositionView>?,
    val answerSheetType: String,
    val choices: List<AnswerChoiceView>,
)