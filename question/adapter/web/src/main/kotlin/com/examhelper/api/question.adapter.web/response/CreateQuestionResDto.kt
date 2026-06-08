package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.CreateQuestionResult

data class CreateQuestionResDto(
    val questionId: String,
    val questionItemIds: List<String>
) {
    companion object {
        fun fromResult(result: CreateQuestionResult) = CreateQuestionResDto(
            questionId = result.questionId.toString(),
            questionItemIds = result.questionItemIds.map { it.toString() }
        )
    }
}
