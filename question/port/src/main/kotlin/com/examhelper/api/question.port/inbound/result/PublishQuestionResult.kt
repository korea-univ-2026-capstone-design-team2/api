package com.examhelper.api.question.port.inbound.result

import com.examhelper.api.question.domain.type.QuestionStatus

data class PublishQuestionResult(
    val questionId: Long,
    val status: QuestionStatus
)
