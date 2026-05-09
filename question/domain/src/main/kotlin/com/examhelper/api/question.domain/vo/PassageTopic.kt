package com.examhelper.api.question.domain.vo

import com.examhelper.api.kernel.type.TopicCategory

data class PassageTopic(
    val category: TopicCategory,
    val keyword: String?,
)

