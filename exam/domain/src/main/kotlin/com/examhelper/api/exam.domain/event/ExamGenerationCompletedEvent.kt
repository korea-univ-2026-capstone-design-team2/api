package com.examhelper.api.exam.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class ExamGenerationCompletedEvent(
    val examId: Long,
    val itemCount: Int,
    val successCount: Int,
    val failCount: Int,
    val occurredAt: Instant = Instant.now()
) : DomainEvent(
    aggregateId = examId.toString(),
    aggregateType = "Exam",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String = "ExamGenerationCompleted"
    override fun topic(): String = "exam.generation-completed"
}

