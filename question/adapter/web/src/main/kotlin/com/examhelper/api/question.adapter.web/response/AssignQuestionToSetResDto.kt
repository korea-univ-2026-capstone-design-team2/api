package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.AssignQuestionToSetResult

data class AssignQuestionToSetResDto(
    val questionId: Long,
    val questionSetId: Long
) {
    companion object {
        fun fromResult(result: AssignQuestionToSetResult) = AssignQuestionToSetResDto(
            questionId = result.questionId,
            questionSetId = result.questionSetId
        )
    }
}
