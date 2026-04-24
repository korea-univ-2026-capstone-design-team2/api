package com.examhelper.api.question.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class QuestionRejectedEvent(
    val questionId: Long,
    val generationId: Long,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId = questionId.toString(),
    aggregateType = "Question",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String = "QuestionRejected"
    override fun topic(): String = "question.rejected"
}
