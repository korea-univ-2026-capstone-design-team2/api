package com.examhelper.api.exam.port.outbound

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.QuestionGenerationId

interface ExamStore {
    fun save(exam: Exam)
    fun saveAll(exams: List<Exam>)
    fun loadById(examId: ExamId): Exam?
    fun loadByGenerationId(generationId: QuestionGenerationId): Exam?
    fun loadByGenerationIdForUpdate(generationId: QuestionGenerationId): Exam?
}
