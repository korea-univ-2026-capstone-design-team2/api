package com.examhelper.api.question.port.inbound.command

/**
 * ~~~Command는 UseCase에서 필요한 필드를 제공합니다.
 */
data class CreateQuestionCommand(
    val content: String
)
