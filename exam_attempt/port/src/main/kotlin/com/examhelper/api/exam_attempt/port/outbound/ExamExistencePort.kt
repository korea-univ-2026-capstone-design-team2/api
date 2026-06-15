package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId

interface ExamExistencePort {
    fun existsById(examId: ExamId, memberId: MemberId): Boolean
}
