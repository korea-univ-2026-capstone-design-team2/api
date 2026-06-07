package com.examhelper.api.learning_statistics.port.inbound.result

import java.time.LocalDate

data class DailyLearningRecordResult(
    val date: LocalDate,
    val questionCount: Long,
    val correctCount: Long,
    val studySeconds: Long,
    val accuracy: Double
)