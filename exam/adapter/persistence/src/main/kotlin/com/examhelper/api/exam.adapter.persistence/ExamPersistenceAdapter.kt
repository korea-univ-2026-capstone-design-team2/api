package com.examhelper.api.exam.adapter.persistence

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.kernel.identifier.ExamId
import org.springframework.stereotype.Repository

@Repository
class ExamPersistenceAdapter(
    private val examJpaRepository: ExamJpaStore
) : ExamStore {
    override fun save(exam: Exam) {
        val entity = ExamEntity.fromDomain(exam)
        examJpaRepository.save(entity)
    }

    override fun saveAll(exams: List<Exam>) {
        val entities = exams.map(ExamEntity::fromDomain)
        examJpaRepository.saveAll(entities)
    }

    override fun loadById(examId: ExamId): Exam? {
        return examJpaRepository.findById(examId.value).orElse(null)?.toDomain()
    }
}
