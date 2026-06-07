package com.examhelper.api.learning_statistics.adapter.web.dto.response

import java.time.LocalDate

data class DailyLearningRecordDto(
    val date: LocalDate,
    val questionCount: Long,
    val correctCount: Long,
    val studySeconds: Long,
    val accuracy: Double,
)
