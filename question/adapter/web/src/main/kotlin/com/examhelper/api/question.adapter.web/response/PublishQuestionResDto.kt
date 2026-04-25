package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.PublishQuestionResult

data class PublishQuestionResDto(
    val questionId: Long,
    val status: String
) {
    companion object {
        fun fromResult(result: PublishQuestionResult) = PublishQuestionResDto(
            questionId = result.questionId,
            status = result.status.name
        )
    }
}
