package com.examhelper.api.exam.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class ExamGenerationFailedEvent(
    val examId: Long,
    val reason: String,
    val occurredAt: Instant = Instant.now()
) : DomainEvent(
    aggregateId = examId.toString(),
    aggregateType = "Exam",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String = "ExamGenerationFailed"
    override fun topic(): String = "exam.generation-failed"
}
