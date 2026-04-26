package com.examhelper.api.question.port.inbound.command

data class AssignQuestionToSetCommand(
    val questionId: Long,
    val questionSetId: Long
)
