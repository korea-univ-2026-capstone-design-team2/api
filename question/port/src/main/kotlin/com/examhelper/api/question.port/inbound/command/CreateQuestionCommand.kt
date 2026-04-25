package com.examhelper.api.question.port.inbound.command

import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionSubType
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.FrameReference
import com.examhelper.api.question.domain.vo.Passage
import com.examhelper.api.question.domain.vo.PassageTopic

/**
 * ~~~Command는 UseCase에서 필요한 필드를 제공합니다.
 */
data class CreateQuestionCommand(
    val generationId: Long,
    val stem: String,
    val passage: Passage?,
    val exhibit: Exhibit?,
    val answerSheet: AnswerSheet,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val passageTopic: PassageTopic?,
    val explanation: Explanation,
    val sourceFrame: FrameReference,
)
