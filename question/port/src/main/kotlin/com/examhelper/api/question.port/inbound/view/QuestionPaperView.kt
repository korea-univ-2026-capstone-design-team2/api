package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionSubType
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject

data class QuestionPaperView(
    val questionId: Long,
    val questionSetId: Long?,

    // 메타데이터
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,

    // 문제 본문
    val stem: String,
    val passageType: String?,
    val passageContent: String?,

    // 보기
    val exhibitType: String?,
    val exhibitContent: String?,
    val propositions: List<QuestionPropositionView>?,

    // 선지
    val answerSheetType: String,
    val choices: List<AnswerChoiceView>,
)
