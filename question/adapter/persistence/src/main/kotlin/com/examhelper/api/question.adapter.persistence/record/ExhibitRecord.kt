package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.Exhibit

class ExhibitRecord(
    val type: String,
    val propositions: List<PropositionRecord>?,
    val content: String?,
) {
    fun toDomain(): Exhibit = when (type) {
        "PROPOSITION" -> Exhibit.PropositionExhibit(
            propositions = requireNotNull(propositions) { "PropositionExhibit.propositions must not be null" }
                .map { it.toDomain() }
        )
        "TEXT" -> Exhibit.TextExhibit(
            content = requireNotNull(content) { "TextExhibit.content must not be null" }
        )
        else -> error("Unknown Exhibit type: $type")
    }

    companion object {
        fun fromDomain(domain: Exhibit): ExhibitRecord = when (domain) {
            is Exhibit.PropositionExhibit -> ExhibitRecord(
                type = "PROPOSITION",
                propositions = domain.propositions.map { PropositionRecord.fromDomain(it) },
                content = null,
            )
            is Exhibit.TextExhibit -> ExhibitRecord(
                type = "TEXT",
                propositions = null,
                content = domain.content,
            )
        }
    }
}
