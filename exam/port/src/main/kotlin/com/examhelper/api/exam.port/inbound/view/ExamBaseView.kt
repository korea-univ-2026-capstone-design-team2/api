package com.examhelper.api.exam.port.inbound.view

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import java.time.Instant

data class ExamBaseView(
    val examId: Long,
    val title: String,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topicCategory: TopicCategory,
    val topicKeyword: String?,
    val topicDescription: String?,
    val targetQuestionCount: Int,
    val status: ExamStatus,
    val generationId: Long?,
    val generationSuccessCount: Int?,
    val generationFailCount: Int?,
    val createdAt: Instant,
    val updatedAt: Instant
)
