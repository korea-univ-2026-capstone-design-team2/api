package com.examhelper.api.question.port.inbound.command

import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.QuestionMetadata
import com.examhelper.api.question.domain.vo.SharedQuestionContext

/**
 * ~~~Command는 UseCase에서 필요한 필드를 제공합니다.
 */
data class CreateQuestionCommand(
    val generationId: Long,
    val sharedContext: SharedQuestionContext?,
    val metadata: QuestionMetadata,
    val questions: List<QuestionCommand>,
) {
    data class QuestionCommand(
        val stem: String,
        val exhibit: Exhibit?,
        val answerSheet: AnswerSheet,
        val explanation: Explanation,
    )
}
