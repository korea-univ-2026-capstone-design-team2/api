package com.examhelper.api.question.port.inbound.result

import com.examhelper.api.question.domain.type.QuestionStatus

data class RejectQuestionResult(
    val questionId: Long,
    val status: QuestionStatus
)
