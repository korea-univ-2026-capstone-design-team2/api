package com.examhelper.api.learning_statistics.port.inbound.view

import java.time.LocalDate

data class DailyLearningRecordView(
    val date: LocalDate,
    val questionCount: Long,
    val correctCount: Long,
    val studySeconds: Long
)
