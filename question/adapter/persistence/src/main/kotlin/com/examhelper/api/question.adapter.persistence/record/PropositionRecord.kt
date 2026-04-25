package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.Proposition
import com.examhelper.api.question.domain.vo.PropositionLabel

class PropositionRecord (
    val label: String,
    val content: String,
) {
    fun toDomain(): Proposition = Proposition(
        label = PropositionLabel.valueOf(label),
        content = content,
    )

    companion object {
        fun fromDomain(domain: Proposition): PropositionRecord = PropositionRecord(
            label = domain.label.name,
            content = domain.content,
        )
    }
}
