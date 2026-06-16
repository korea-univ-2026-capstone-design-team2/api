package com.examhelper.api.kernel.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.time.Instant

class ExamAttemptSubmittedEvent(
    val attemptId: Long,
    val examId: Long,
    val memberId: Long,
    val submittedAt: Instant,
    val items: List<ExamAttemptSubmittedItem>
) : DomainEvent(
    aggregateId = attemptId.toString(),
    aggregateType = "ExamAttempt",
    channel = EventChannel.EXTERNAL
) {
    override val eventType = "ExamAttemptSubmitted"
    override fun topic(): String = "exam-attempt.submitted"
}
