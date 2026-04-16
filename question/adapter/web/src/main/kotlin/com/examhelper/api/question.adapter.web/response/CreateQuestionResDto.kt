package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.`in`.result.CreateQuestionResult

data class CreateQuestionResDto(
    val questionId: Long
) {
    companion object {
        fun fromResult(result: CreateQuestionResult): CreateQuestionResDto {
            return CreateQuestionResDto(
                questionId = result.questionId
            )
        }
    }
}
