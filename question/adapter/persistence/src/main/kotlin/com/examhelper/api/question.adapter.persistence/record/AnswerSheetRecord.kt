package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.AnswerSheet

class AnswerSheetRecord(
    val type: String,
    val correctNumber: Int?,
    val choices: List<AnswerChoiceRecord>?,
) {
    fun toDomain(): AnswerSheet = when (type) {
        "MULTIPLE_CHOICE" -> AnswerSheet.MultipleChoiceSheet(
            correctNumber = requireNotNull(correctNumber) { "MultipleChoiceSheet.correctNumber must not be null" },
            choices = requireNotNull(choices) { "MultipleChoiceSheet.choices must not be null" }
                .map { it.toDomain() },
        )
        else -> error("Unknown AnswerSheet type: $type")
    }

    companion object {
        fun fromDomain(domain: AnswerSheet): AnswerSheetRecord = when (domain) {
            is AnswerSheet.MultipleChoiceSheet -> AnswerSheetRecord(
                type = "MULTIPLE_CHOICE",
                correctNumber = domain.correctNumber,
                choices = domain.choices.map { AnswerChoiceRecord.fromDomain(it) },
            )
        }
    }
}
