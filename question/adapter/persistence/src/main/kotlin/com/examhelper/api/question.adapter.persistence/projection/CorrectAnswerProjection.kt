package com.examhelper.api.question.adapter.persistence.projection

interface CorrectAnswerProjection {
    val questionItemId: Long
    val correctNumber: Int
}
