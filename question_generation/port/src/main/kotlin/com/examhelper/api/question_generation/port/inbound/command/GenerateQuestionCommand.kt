package com.examhelper.api.question_generation.port.inbound.command

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory

data class GenerateQuestionCommand(
    val subject: Subject,
    val questionType: QuestionType?,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel?,
    val topicCategory: TopicCategory?,
    val topicKeyword: String?,
    val topicDescription: String?,
    val quantity: Int,
    val frameSearchTopK: Int = 3
)
