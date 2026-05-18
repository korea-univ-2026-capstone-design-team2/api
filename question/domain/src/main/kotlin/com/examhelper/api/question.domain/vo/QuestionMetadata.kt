package com.examhelper.api.question.domain.vo

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionMetadata(
    val subject: Subject,
    val questionType: QuestionType,
    val difficulty: DifficultyLevel,
    val passageTopic: PassageTopic?,
)
