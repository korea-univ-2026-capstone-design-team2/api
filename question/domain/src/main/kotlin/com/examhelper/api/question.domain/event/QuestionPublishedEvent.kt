package com.examhelper.api.question.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class QuestionPublishedEvent(
    val questionId: Long,
    val subject: String,
    val questionType: String,
    val difficulty: String,
    val occurredAt: Instant,
) : DomainEvent(
    aggregateId = questionId.toString(),
    aggregateType = "Question",
    channel = EventChannel.EXTERNAL,
) {
    override val eventType: String = "QuestionPublished"
    override fun topic(): String = "question.published"
}
