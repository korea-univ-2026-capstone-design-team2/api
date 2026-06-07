package com.examhelper.api.learning_statistics.adapter.persistence.projection.projection

import java.time.LocalDate

interface DailyLearningRecordProjection {
    val date: LocalDate
    val questionCount: Long
    val correctCount: Long
    val studySeconds: Long
}