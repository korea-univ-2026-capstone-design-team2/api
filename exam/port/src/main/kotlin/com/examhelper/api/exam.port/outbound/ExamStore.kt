package com.examhelper.api.exam.port.outbound

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.kernel.identifier.ExamId

interface ExamStore {
    fun save(exam: Exam)
    fun saveAll(exams: List<Exam>)
    fun loadById(examId: ExamId): Exam?
}
