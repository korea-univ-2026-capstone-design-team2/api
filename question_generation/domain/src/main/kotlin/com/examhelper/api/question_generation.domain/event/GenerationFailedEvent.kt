package com.examhelper.api.question_generation.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class GenerationFailedEvent(
    val generationId: Long,
    val reason: String,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId   = generationId.toString(),
    aggregateType = "QuestionGeneration",
    channel       = EventChannel.INTERNAL,
) {
    override val eventType: String = "GenerationFailed"
    override fun topic(): String   = "question-generation.failed"
}
