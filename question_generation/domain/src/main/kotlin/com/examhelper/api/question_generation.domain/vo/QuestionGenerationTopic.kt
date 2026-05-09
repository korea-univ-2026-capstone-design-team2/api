package com.examhelper.api.question_generation.domain.vo

import com.examhelper.api.kernel.type.TopicCategory

data class QuestionGenerationTopic(
    val category: TopicCategory,
    val keyword: String?,
    val description: String?
)
