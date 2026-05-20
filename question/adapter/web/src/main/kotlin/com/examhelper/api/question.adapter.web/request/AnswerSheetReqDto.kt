package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.kernel.type.PropositionLabel
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet

sealed class AnswerSheetReqDto {
    abstract fun toDomain(): AnswerSheet

    data class MultipleChoiceSheetReqDto(
        val choices: List<AnswerChoiceReqDto>,
        val correctNumber: Int,
    ) : AnswerSheetReqDto() {
        override fun toDomain(): AnswerSheet =
            AnswerSheet.MultipleChoiceSheet(
                choices = choices.map { it.toDomain() },
                correctNumber = correctNumber,
            )
    }

    sealed class AnswerChoiceReqDto {
        abstract fun toDomain(): AnswerChoice

        data class TextChoiceReqDto(
            val number: Int,
            val isCorrect: Boolean,
            val content: String,
        ) : AnswerChoiceReqDto() {
            override fun toDomain(): AnswerChoice =
                AnswerChoice.TextChoice(
                    number = number,
                    isCorrect = isCorrect,
                    content = content,
                )
        }

        data class PropositionCombinationChoiceReqDto(
            val number: Int,
            val isCorrect: Boolean,
            val labels: List<PropositionLabel>,
        ) : AnswerChoiceReqDto() {
            override fun toDomain(): AnswerChoice =
                AnswerChoice.PropositionCombinationChoice(
                    number = number,
                    isCorrect = isCorrect,
                    labels = labels,
                )
        }
    }
}
