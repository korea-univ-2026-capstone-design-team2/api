package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.Passage

class PassageRecord(
    val type: String,
    val content: String?,
    val description: String?,
) {
    fun toDomain(): Passage = when (type) {
        "TEXT" -> Passage.TextPassage(
            content = requireNotNull(content) { "TextPassage.content must not be null" },
            description = description,
        )
        else -> error("Unknown Passage type: $type")
    }

    companion object {
        fun fromDomain(domain: Passage): PassageRecord = when (domain) {
            is Passage.TextPassage -> PassageRecord(
                type = "TEXT",
                content = domain.content,
                description = domain.description,
            )
        }
    }
}
