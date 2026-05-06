package com.examhelper.api.question_generation.port.inbound.command

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationTopic

data class GenerateQuestionCommand(
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topicCategory: String,
    val topicKeyword: String?,
    val topicDescription: String?,
    val quantity: Int,
    val frameSearchTopK: Int = 3
) {
    fun toGenerationRequest(): QuestionGenerationRequest =
        QuestionGenerationRequest(
            subject         = subject,
            questionType    = questionType,
            questionSubType = questionSubType,
            difficulty      = difficulty,
            topic           = QuestionGenerationTopic(
                category    = topicCategory,
                keyword     = topicKeyword,
                description = topicDescription,
            ),
            quantity        = quantity,
            frameSearchTopK = frameSearchTopK,
        )
}
