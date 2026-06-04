package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.kernel.identifier.ExamId

interface ExamExistencePort {
    fun existsById(examId: ExamId): Boolean
}
