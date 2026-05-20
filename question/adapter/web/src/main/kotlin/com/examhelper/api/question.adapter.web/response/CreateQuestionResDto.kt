package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.CreateQuestionResult

data class CreateQuestionResDto(
    val questionId: Long,
    val questionItemIds: List<Long>,
) {
    companion object {
        fun fromResult(result: CreateQuestionResult) = CreateQuestionResDto(
            questionId = result.questionId,
            questionItemIds = result.questionItemIds,
        )
    }
}
