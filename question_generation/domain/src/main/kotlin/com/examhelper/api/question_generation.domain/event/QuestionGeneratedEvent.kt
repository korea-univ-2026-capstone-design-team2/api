package com.examhelper.api.question_generation.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class QuestionGeneratedEvent(
    val generationId: Long,
    val questionId: Long,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId   = generationId.toString(),
    aggregateType = "QuestionGeneration",
    channel       = EventChannel    .INTERNAL,
) {
    override val eventType: String = "QuestionGenerated"
    override fun topic(): String   = "question-generation.question-generated"
}
