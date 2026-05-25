package com.examhelper.api.exam.domain.entity

import com.examhelper.api.kernel.identifier.ExamItemId
import com.examhelper.api.kernel.identifier.QuestionId
import java.time.Instant

class ExamItem(
    val id: ExamItemId,
    val questionId: QuestionId,
    val ordering: Int,
    val createdAt: Instant = Instant.now(),
) {
    init {
        require(ordering > 0) { "ordering must be positive, got $ordering" }
    }

    override fun equals(other: Any?): Boolean =
        other is ExamItem && id == other.id

    override fun hashCode(): Int = id.hashCode()
}
