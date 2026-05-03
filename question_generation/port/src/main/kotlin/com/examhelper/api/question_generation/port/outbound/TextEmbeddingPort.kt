package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.question_generation.port.outbound.command.TextEmbeddingCommand
import com.examhelper.api.question_generation.port.outbound.result.TextEmbeddingResult

interface TextEmbeddingPort {
    fun embed(command: TextEmbeddingCommand): TextEmbeddingResult
}
