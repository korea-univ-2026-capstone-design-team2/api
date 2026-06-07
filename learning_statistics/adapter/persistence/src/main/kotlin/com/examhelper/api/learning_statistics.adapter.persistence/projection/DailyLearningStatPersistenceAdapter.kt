package com.examhelper.api.learning_statistics.adapter.persistence.projection

import com.examhelper.api.kernel.identifier.DailyLearningStatId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.learning_statistics.domain.DailyLearningStat
import com.examhelper.api.learning_statistics.port.outbound.DailyLearningStatStore
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DailyLearningStatPersistenceAdapter(
    private val jpaRepository: DailyLearningStatJpaStore
) : DailyLearningStatStore {
    override fun save(stat: DailyLearningStat): DailyLearningStat =
        jpaRepository.save(DailyLearningStatEntity.fromDomain(stat)).toDomain()

    override fun loadById(id: DailyLearningStatId): DailyLearningStat? =
        jpaRepository.findById(id.value).orElse(null)?.toDomain()

    override fun loadByMemberAndDateAndSubject(
        memberId: MemberId,
        date: LocalDate,
        subject: Subject
    ): DailyLearningStat? =
        jpaRepository.findByMemberIdAndStudyDateAndSubject(
            memberId = memberId.value,
            studyDate = date,
            subject = subject,
        )?.toDomain()
}
