package com.examhelper.api.exam.domain.vo

import com.examhelper.api.exam.domain.type.ExamTopic
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class ExamMetadata(
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topic: ExamTopic,
    val targetQuestionCount: Int
)
