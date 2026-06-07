package com.examhelper.api.learning_statistics.port.outbound

import com.examhelper.api.kernel.identifier.DailyLearningStatId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.learning_statistics.domain.DailyLearningStat
import java.time.LocalDate

interface DailyLearningStatStore {
    fun save(stat: DailyLearningStat): DailyLearningStat
    fun loadById(id: DailyLearningStatId): DailyLearningStat?
    fun loadByMemberAndDateAndSubject(
        memberId: MemberId,
        date: LocalDate,
        subject: Subject,
    ): DailyLearningStat?
}
