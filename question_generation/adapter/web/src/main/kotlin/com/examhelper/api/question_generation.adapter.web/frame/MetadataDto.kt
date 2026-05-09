package com.examhelper.api.question_generation.adapter.web.frame

data class MetadataDto(
    val questionType: String,
    val questionSubType: String?,
    val difficulty: String,
    val topicCategory: String,
    val topicKeyword: String?,
)
