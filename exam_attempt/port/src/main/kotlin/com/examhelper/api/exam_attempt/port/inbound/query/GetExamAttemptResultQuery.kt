package com.examhelper.api.exam_attempt.port.inbound.query

data class GetExamAttemptResultQuery(
    val attemptId: Long,
    val memberId: Long
)
