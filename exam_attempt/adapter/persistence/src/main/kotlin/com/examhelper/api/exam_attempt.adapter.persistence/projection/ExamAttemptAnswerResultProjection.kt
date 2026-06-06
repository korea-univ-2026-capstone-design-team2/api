package com.examhelper.api.exam_attempt.adapter.persistence.projection

data class ExamAttemptAnswerResultProjection(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean
)
