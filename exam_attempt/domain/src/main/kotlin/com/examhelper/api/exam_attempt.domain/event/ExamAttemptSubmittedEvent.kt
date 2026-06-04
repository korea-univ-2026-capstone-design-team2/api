package com.examhelper.api.exam_attempt.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class ExamAttemptSubmittedEvent(
    val attemptId: Long,
    val examId: Long,
    val memberId: Long,
    val occurredAt: Instant
) : DomainEvent(
    aggregateId = attemptId.toString(),
    aggregateType = "ExamAttempt",
    channel = EventChannel.INTERNAL
) {
    override val eventType = "ExamAttemptSubmitted"
    override fun topic(): String = "exam-attempt.submitted"
}
