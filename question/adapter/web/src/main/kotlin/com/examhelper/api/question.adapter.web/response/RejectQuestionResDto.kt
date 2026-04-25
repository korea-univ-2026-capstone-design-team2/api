package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.RejectQuestionResult

data class RejectQuestionResDto(
    val questionId: Long,
    val status: String
) {
    companion object {
        fun fromResult(result: RejectQuestionResult) = RejectQuestionResDto(
            questionId = result.questionId,
            status = result.status.name
        )
    }
}
