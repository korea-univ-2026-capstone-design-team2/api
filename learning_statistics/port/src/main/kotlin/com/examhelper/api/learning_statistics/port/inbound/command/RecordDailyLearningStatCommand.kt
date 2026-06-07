package com.examhelper.api.learning_statistics.port.inbound.command

import com.examhelper.api.kernel.event.ExamAttemptSubmittedItem
import java.time.Instant

data class RecordDailyLearningStatCommand(
    val memberId: Long,
    val submittedAt: Instant,
    val items: List<ExamAttemptSubmittedItem>
)
