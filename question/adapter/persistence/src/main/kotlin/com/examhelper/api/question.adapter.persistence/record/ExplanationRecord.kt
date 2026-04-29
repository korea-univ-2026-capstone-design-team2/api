package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.Explanation

class ExplanationRecord(
    val correctReason: String,
    val incorrectReasons: Map<String, String>,
) {
    fun toDomain(): Explanation = Explanation(
        correctReason = correctReason,
        incorrectReasons = incorrectReasons.mapKeys { it.key.toInt() },
    )

    companion object {
        fun fromDomain(domain: Explanation): ExplanationRecord = ExplanationRecord(
            correctReason = domain.correctReason,
            incorrectReasons = domain.incorrectReasons.mapKeys { it.key.toString() },
        )
    }
}
