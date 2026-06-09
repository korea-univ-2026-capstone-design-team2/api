package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.domain.ExamAttempt
import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptStore
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import org.springframework.stereotype.Repository

@Repository
class ExamAttemptPersistenceAdapter(
    private val jpaStore: ExamAttemptJpaStore,
) : ExamAttemptStore {
    override fun save(attempt: ExamAttempt) {
        jpaStore.save(ExamAttemptEntity.fromDomain(attempt))
    }

    override fun loadById(id: ExamAttemptId): ExamAttempt? =
        jpaStore.findById(id.value).orElse(null)?.toDomain()

    override fun loadByExamIdAndMemberId(
        examId: ExamId,
        memberId: MemberId,
    ): ExamAttempt? =
        jpaStore.findByExamIdAndMemberId(examId.value, memberId.value)?.toDomain()

    override fun loadInProgressByExamIdAndMemberId(
        examId: ExamId,
        memberId: MemberId,
    ): ExamAttempt? =
        jpaStore.findByExamIdAndMemberIdAndStatus(
            examId = examId.value,
            memberId = memberId.value,
            status = ExamAttemptStatus.IN_PROGRESS,
        )?.toDomain()
}
