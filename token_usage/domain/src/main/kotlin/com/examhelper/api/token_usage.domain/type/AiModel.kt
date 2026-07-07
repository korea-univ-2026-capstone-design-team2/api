package com.examhelper.api.token_usage.domain.type

enum class AiModel(
    val provider: AiProvider
) {
    GPT_5_MINI(AiProvider.OPENAI),
    GPT_5_4_MINI(AiProvider.OPENAI),
    CLAUDE_HAIKU_4_5(AiProvider.ANTHROPIC);

    companion object {
        fun fromModelName(modelName: String): AiModel =
            when (modelName) {
                "gpt-5-mini" -> GPT_5_MINI
                "gpt-5.4-mini" -> GPT_5_4_MINI
                "claude-haiku-4-5" -> CLAUDE_HAIKU_4_5
                else -> throw IllegalArgumentException(
                    "지원하지 않는 AI 모델입니다. model=$modelName"
                )
            }
    }
}
