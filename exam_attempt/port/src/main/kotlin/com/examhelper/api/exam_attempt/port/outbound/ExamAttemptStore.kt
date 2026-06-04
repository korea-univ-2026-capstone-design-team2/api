package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.exam_attempt.domain.ExamAttempt
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId

interface ExamAttemptStore {
    fun save(attempt: ExamAttempt)
    fun loadById(id: ExamAttemptId): ExamAttempt?
    fun loadByExamIdAndMemberId(examId: ExamId, memberId: MemberId): ExamAttempt?
    fun loadInProgressByExamIdAndMemberId(examId: ExamId, memberId: MemberId): ExamAttempt?
}
