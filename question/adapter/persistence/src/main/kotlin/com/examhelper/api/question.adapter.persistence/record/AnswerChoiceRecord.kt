package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.PropositionLabel

class AnswerChoiceRecord (
    val type: String,
    val number: Int,
    val isCorrect: Boolean,
    val content: String?,
    val labels: List<String>?,
) {
    fun toDomain(): AnswerChoice = when (type) {
        "TEXT" -> AnswerChoice.TextChoice(
            number = number,
            isCorrect = isCorrect,
            content = requireNotNull(content) { "TextChoice.content must not be null" },
        )
        "PROPOSITION_COMBINATION" -> AnswerChoice.PropositionCombinationChoice(
            number = number,
            isCorrect = isCorrect,
            labels = requireNotNull(labels) { "PropositionCombinationChoice.labels must not be null" }
                .map { PropositionLabel.valueOf(it) },
        )
        else -> error("Unknown AnswerChoice type: $type")
    }

    companion object {
        fun fromDomain(domain: AnswerChoice): AnswerChoiceRecord = when (domain) {
            is AnswerChoice.TextChoice -> AnswerChoiceRecord(
                type = "TEXT",
                number = domain.number,
                isCorrect = domain.isCorrect,
                content = domain.content,
                labels = null,
            )
            is AnswerChoice.PropositionCombinationChoice -> AnswerChoiceRecord(
                type = "PROPOSITION_COMBINATION",
                number = domain.number,
                isCorrect = domain.isCorrect,
                content = null,
                labels = domain.labels.map { it.name },
            )
        }
    }
}
