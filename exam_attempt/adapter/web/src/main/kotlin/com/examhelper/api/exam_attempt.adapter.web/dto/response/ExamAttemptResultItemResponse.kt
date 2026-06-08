package com.examhelper.api.exam_attempt.adapter.web.dto.response

import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultItemView

data class ExamAttemptResultItemResponse(
    val questionItemId: String,
    val selectedNumber: Int?,
    val correctNumber: Int,
    val correct: Boolean,
    val timeSpentSeconds: Int
) {
    companion object {
        fun fromView(view: ExamAttemptResultItemView): ExamAttemptResultItemResponse =
            ExamAttemptResultItemResponse(
                questionItemId = view.questionItemId.toString(),
                selectedNumber = view.selectedNumber,
                correctNumber = view.correctNumber,
                correct = view.correct,
                timeSpentSeconds = view.timeSpentSeconds
            )
    }
}
