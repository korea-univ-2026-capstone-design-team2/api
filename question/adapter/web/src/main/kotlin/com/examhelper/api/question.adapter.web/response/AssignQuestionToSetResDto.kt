package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.AssignQuestionToSetResult

data class AssignQuestionToSetResDto(
    val questionId: String,
    val questionSetId: String
) {
    companion object {
        fun fromResult(result: AssignQuestionToSetResult) = AssignQuestionToSetResDto(
            questionId = result.questionId.toString(),
            questionSetId = result.questionSetId.toString()
        )
    }
}
