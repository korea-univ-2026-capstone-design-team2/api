package com.examhelper.api.exam.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class ExamCreatedEvent(
    val examId: Long,
    val subject: String,
    val questionType: String,
    val difficulty: String,
    val targetQuestionCount: Int,
    val occurredAt: Instant = Instant.now()
) : DomainEvent(
    aggregateId = examId.toString(),
    aggregateType = "Exam",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String = "ExamCreated"
    override fun topic(): String = "exam.created"
}
