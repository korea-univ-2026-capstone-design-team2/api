package com.examhelper.api.question_generation.adapter.ai.mapper

import com.examhelper.api.question_generation.port.outbound.result.LlmTokenUsageResult
import org.springframework.ai.chat.metadata.Usage
import org.springframework.stereotype.Component

@Component
class LlmTokenUsageMapper {
    fun toDomain(
        model: String,
        usage: Usage,
    ): LlmTokenUsageResult =
        LlmTokenUsageResult(
            model = model,
            promptTokens = usage.promptTokens,
            completionTokens = usage.completionTokens,
            totalTokens = usage.totalTokens,
        )
}
