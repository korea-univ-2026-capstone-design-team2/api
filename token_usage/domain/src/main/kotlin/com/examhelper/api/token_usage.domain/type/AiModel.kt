package com.examhelper.api.token_usage.domain.type

enum class AiModel(
    val provider: AiProvider
) {
    GPT_5_MINI(AiProvider.OPENAI),
    GPT_5_4_MINI(AiProvider.OPENAI),
    CLAUDE_HAIKU_4_5(AiProvider.ANTHROPIC)
}
