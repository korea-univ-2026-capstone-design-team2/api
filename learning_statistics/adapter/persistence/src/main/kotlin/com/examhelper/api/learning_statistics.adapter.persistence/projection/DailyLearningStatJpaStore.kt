package com.examhelper.api.learning_statistics.adapter.persistence.projection

import com.examhelper.api.kernel.type.Subject
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DailyLearningStatJpaStore : JpaRepository<DailyLearningStatEntity, Long> {
    fun findByMemberIdAndStudyDateAndSubject(
        memberId: Long,
        studyDate: LocalDate,
        subject: Subject
    ): DailyLearningStatEntity?
}
