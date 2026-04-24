package com.examhelper.api.question.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class QuestionCreatedEvent(
    val questionId: Long,
    val generationId: Long,
    val subject: String,
    val questionType: String,
    val questionSubType: String?,
    val difficulty: String,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId = questionId.toString(),
    aggregateType = "Question",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String = "QuestionCreated"
    override fun topic(): String = "question.created"
}
