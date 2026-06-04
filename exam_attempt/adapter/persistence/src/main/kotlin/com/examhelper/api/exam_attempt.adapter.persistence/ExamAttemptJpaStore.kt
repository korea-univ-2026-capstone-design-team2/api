package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ExamAttemptJpaStore : JpaRepository<ExamAttemptEntity, Long> {
    fun findByExamIdAndMemberId(
        examId: Long,
        memberId: Long
    ): ExamAttemptEntity?

    fun findByExamIdAndMemberIdAndStatus(
        examId: Long,
        memberId: Long,
        status: ExamAttemptStatus
    ): ExamAttemptEntity?
}
