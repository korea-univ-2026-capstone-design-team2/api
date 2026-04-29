package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.QuestionContent

data class QuestionContentRecord(
    val stem: String,
    val passage: PassageRecord?,
    val exhibit: ExhibitRecord?,
) {
    fun toDomain(): QuestionContent = QuestionContent(
        stem = stem,
        passage = passage?.toDomain(),
        exhibit = exhibit?.toDomain(),
    )

    companion object {
        fun fromDomain(domain: QuestionContent): QuestionContentRecord = QuestionContentRecord(
            stem = domain.stem,
            passage = domain.passage?.let { PassageRecord.fromDomain(it) },
            exhibit = domain.exhibit?.let { ExhibitRecord.fromDomain(it) },
        )
    }
}
