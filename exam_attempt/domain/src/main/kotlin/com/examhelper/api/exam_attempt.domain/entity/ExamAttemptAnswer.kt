package com.examhelper.api.exam_attempt.domain.entity

import com.examhelper.api.kernel.core.DomainEntity
import com.examhelper.api.kernel.identifier.QuestionItemId

class ExamAttemptAnswer(
    val questionItemId: QuestionItemId,
    selectedNumber: Int?,
    timeSpentSeconds: Int,
    markedUnknown: Boolean,
    bookmarked: Boolean
): DomainEntity<QuestionItemId>(questionItemId) {
    var selectedNumber: Int? = selectedNumber
        private set

    var timeSpentSeconds: Int = timeSpentSeconds
        private set

    var markedUnknown: Boolean = markedUnknown
        private set

    var bookmarked: Boolean = bookmarked
        private set

    fun update(
        selectedNumber: Int?,
        timeSpentSeconds: Int,
        markedUnknown: Boolean,
        bookmarked: Boolean,
    ) {
        this.selectedNumber = selectedNumber
        this.timeSpentSeconds = timeSpentSeconds
        this.markedUnknown = markedUnknown
        this.bookmarked = bookmarked
    }
}
