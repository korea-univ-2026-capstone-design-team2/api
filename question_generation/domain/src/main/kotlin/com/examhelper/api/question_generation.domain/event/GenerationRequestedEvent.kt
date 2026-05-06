package com.examhelper.api.question_generation.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class GenerationRequestedEvent(
    val generationId: Long,
    val subject: String,
    val questionType: String,
    val quantity: Int,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId   = generationId.toString(),
    aggregateType = "QuestionGeneration",
    channel       = EventChannel.INTERNAL,
) {
    override val eventType: String = "GenerationRequested"
    override fun topic(): String   = "question-generation.requested"
}
