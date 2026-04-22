package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand

data class CreateQuestionReqDto(
    val content: String
) {
    fun toCommand(): CreateQuestionCommand {
        return CreateQuestionCommand(
            content = content
        )
    }
}
