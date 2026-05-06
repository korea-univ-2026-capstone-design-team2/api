package com.examhelper.api.question.domain.vo

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.domain.exception.QuestionAssertionException

data class QuestionMetadata(
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val passageTopic: PassageTopic?,
) {
    init { validate() }

    private fun validate() {
        validateSubTypeConsistency()
    }

    // 독해형만 subType 허용, 독해형은 subType 필수
    private fun validateSubTypeConsistency() {
        when (questionType) {
            QuestionType.READING -> {
                require(questionSubType != null) {
                    throw QuestionAssertionException.ReadingSubTypeRequired()
                }
            }
            else -> {
                require(questionSubType == null) {
                    throw QuestionAssertionException.SubTypeMismatch(
                        questionType.name,
                        questionSubType!!.name
                    )
                }
            }
        }
    }
}
