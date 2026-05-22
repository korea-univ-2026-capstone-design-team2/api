package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.kernel.type.PropositionLabel
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Proposition

sealed class ExhibitReqDto {
    abstract fun toDomain(): Exhibit

    data class PropositionExhibitReqDto(
        val propositions: List<PropositionReqDto>,
    ) : ExhibitReqDto() {
        override fun toDomain(): Exhibit =
            Exhibit.PropositionExhibit(
                propositions = propositions.map { it.toDomain() }
            )
    }

    data class TextExhibitReqDto(
        val content: String,
    ) : ExhibitReqDto() {
        override fun toDomain(): Exhibit =
            Exhibit.TextExhibit(content = content)
    }

    data class PropositionReqDto(
        val label: PropositionLabel,
        val content: String
    ) {
        fun toDomain(): Proposition =
            Proposition(label = label, content = content)
    }
}
