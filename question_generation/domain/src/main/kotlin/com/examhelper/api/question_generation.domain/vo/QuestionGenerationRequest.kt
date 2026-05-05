package com.examhelper.api.question_generation.domain.vo

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question_generation.domain.exception.GenerationAssertionException

data class QuestionGenerationRequest(
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topic: QuestionGenerationTopic,
    val quantity: Int,
    val frameSearchTopK: Int = 3
) {
    init {
        require(quantity in 1..20) {
            throw GenerationAssertionException.QuantityOutOfRange(quantity)
        }
        require(frameSearchTopK in 1..10) {
            throw GenerationAssertionException.TopKOutOfRange(frameSearchTopK)
        }
        // question-domain의 QuestionMetadata와 동일한 규칙 재적용
        if (questionType == QuestionType.READING) {
            requireNotNull(questionSubType) {
                throw GenerationAssertionException.ReadingSubTypeRequired()
            }
        } else {
            require(questionSubType == null) {
                throw GenerationAssertionException.SubTypeMismatch(
                    questionType.name, questionSubType!!.name
                )
            }
        }
    }
}
