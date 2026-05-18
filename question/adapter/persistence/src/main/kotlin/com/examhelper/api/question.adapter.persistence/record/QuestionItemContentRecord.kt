package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.QuestionItemContent

data class QuestionItemContentRecord(
    val stem: String,
    val exhibit: ExhibitRecord?
) {
    fun toDomain(): QuestionItemContent = QuestionItemContent(
        stem = stem,
        exhibit = exhibit?.toDomain()
    )

    companion object {
        fun fromDomain(domain: QuestionItemContent): QuestionItemContentRecord = QuestionItemContentRecord(
            stem = domain.stem,
            exhibit = domain.exhibit?.let { ExhibitRecord.fromDomain(it) }
        )
    }
}
