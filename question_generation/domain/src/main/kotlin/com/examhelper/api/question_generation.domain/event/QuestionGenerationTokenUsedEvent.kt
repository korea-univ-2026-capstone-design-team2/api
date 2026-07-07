package com.examhelper.api.question_generation.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class QuestionGenerationTokenUsedEvent(
    val generationId: Long,
    val memberId: Long,
    val model: String,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId = generationId.toString(),
    aggregateType = "QuestionGeneration",
    channel = EventChannel.EXTERNAL,
) {
    override val eventType: String = "QuestionGenerationTokenUsed"

    override fun topic(): String =
        "question-generation.token-used"
}
