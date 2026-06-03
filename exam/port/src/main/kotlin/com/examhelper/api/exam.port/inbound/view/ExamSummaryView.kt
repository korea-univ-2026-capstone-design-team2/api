package com.examhelper.api.exam.port.inbound.view

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import java.time.Instant

data class ExamSummaryView(
    val examId: Long,
    val title: String,
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val targetQuestionCount: Int,
    val actualQuestionCount: Int,
    val status: ExamStatus,
    val createdAt: Instant
)
