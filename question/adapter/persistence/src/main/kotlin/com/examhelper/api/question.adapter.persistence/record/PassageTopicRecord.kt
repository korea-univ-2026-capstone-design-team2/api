package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.PassageTopic

class PassageTopicRecord(
    val category: String,
    val keyword: String?,
) {
    fun toDomain(): PassageTopic = PassageTopic(
        category = category,
        keyword = keyword,
    )

    companion object {
        fun fromDomain(domain: PassageTopic): PassageTopicRecord = PassageTopicRecord(
            category = domain.category,
            keyword = domain.keyword,
        )
    }
}
