package com.examhelper.api.question_generation.domain.vo

import com.examhelper.api.question_generation.domain.exception.GenerationAssertionException

data class QuestionGenerationTopic(
    val category: String,
    val keyword: String?,
    val description: String?
) {
    init {
        require(category.isNotBlank()) {
            throw GenerationAssertionException.TopicCategoryBlank()
        }
    }
}
