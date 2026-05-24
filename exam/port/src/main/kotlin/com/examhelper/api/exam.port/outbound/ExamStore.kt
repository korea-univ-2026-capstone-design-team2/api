package com.examhelper.api.exam.port.outbound

import com.examhelper.api.exam.domain.Exam

interface ExamStore {
    fun save(exam: Exam)
    fun saveAll(exams: List<Exam>)
    fun loadById(examId: String): Exam?
}
