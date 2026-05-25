package com.examhelper.api.exam.domain.type

import com.examhelper.api.kernel.type.TopicCategory

data class ExamTopic(
    val category: TopicCategory,
    val keyword: String?,
    val description: String?
)
