package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.port.inbound.command.AssignQuestionToSetCommand

data class AssignQuestionToSetReqDto(
    val questionSetId: Long
) {
    fun toCommand(questionId: Long) = AssignQuestionToSetCommand(
        questionId = questionId,
        questionSetId = questionSetId,
    )
}
