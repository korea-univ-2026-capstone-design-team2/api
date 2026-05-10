package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.adapter.ai.exception.TextEmbeddingException
import com.examhelper.api.question_generation.port.outbound.TextEmbeddingPort
import com.examhelper.api.question_generation.port.outbound.command.TextEmbeddingCommand
import com.examhelper.api.question_generation.port.outbound.result.TextEmbeddingResult
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.stereotype.Component

@Component
class TextEmbeddingAdapter(
    private val embeddingModel: EmbeddingModel
) : TextEmbeddingPort {
    override fun embed(command: TextEmbeddingCommand): TextEmbeddingResult =
        try {
            val embedding = embeddingModel
                .embed(command.text)
                .map { it }

            return TextEmbeddingResult(embedding)
        } catch (ex: Exception) {
            throw TextEmbeddingException.EmbeddingFailed(ex)
        }
}
