package com.examhelper.api.question_generation.port.outbound.command

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionCreationMetadata(
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topicCategory: String,
    val topicKeyword: String?,
    val frameId: String,
    val similarityScore: Double
)
