package com.examhelper.api.question_generation.adapter.ai.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.ai.openai.chat")
data class OpenAiChatProperties(
    val model: String,
)
