package com.examhelper.api.question_generation.application.factory

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.TopicCategory
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationTopic
import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import org.springframework.stereotype.Component

@Component
class QuestionGenerationRequestFactory {
    fun create(command: GenerateQuestionCommand): QuestionGenerationRequest {
        val questionType = command.questionType ?: QuestionType.entries.random()

        val questionSubType = resolveQuestionSubType(
                questionType = questionType,
                requestedSubType = command.questionSubType
            )

        val difficulty = command.difficulty ?: DifficultyLevel.entries.random()
        val topicCategory = command.topicCategory ?: TopicCategory.entries.random()

        return QuestionGenerationRequest(
            subject = command.subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            topic = QuestionGenerationTopic(
                category = topicCategory,
                keyword = command.topicKeyword,
                description = command.topicDescription
            ),
            quantity = command.quantity
        )
    }

    private fun resolveQuestionSubType(
        questionType: QuestionType,
        requestedSubType: QuestionSubType?
    ): QuestionSubType? {
        if (requestedSubType != null) return requestedSubType

        return questionType.compatibleSubTypes().randomOrNull()
    }
}
