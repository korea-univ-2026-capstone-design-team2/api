package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.question.domain.vo.SharedQuestionContext

sealed class SharedQuestionContextRecord {
    abstract val type: String
    abstract fun toDomain(): SharedQuestionContext

    data class Text(
        override val type: String = "TEXT",
        val content: String,
        val description: String?,
    ) : SharedQuestionContextRecord() {
        override fun toDomain() = SharedQuestionContext.Text(
            content = content,
            description = description,
        )
    }

    companion object {
        fun fromDomain(domain: SharedQuestionContext?): SharedQuestionContextRecord? = when (domain) {
            is SharedQuestionContext.Text -> Text(
                content = domain.content,
                description = domain.description,
            )
            null -> null
        }
    }
}
