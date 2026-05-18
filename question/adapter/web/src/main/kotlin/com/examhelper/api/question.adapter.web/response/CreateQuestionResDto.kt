package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.CreateQuestionResult

data class CreateQuestionResDto(
    val groupId: Long,
    val questionIds: List<Long>,
) {
    companion object {
        fun fromResult(result: CreateQuestionResult) = CreateQuestionResDto(
            groupId = result.questionId,
            questionIds = result.questionItemIds,
        )
    }
}
