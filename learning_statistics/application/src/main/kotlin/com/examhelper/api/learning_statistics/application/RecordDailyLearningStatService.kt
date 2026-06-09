package com.examhelper.api.learning_statistics.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.DailyLearningStatId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.learning_statistics.domain.DailyLearningStat
import com.examhelper.api.learning_statistics.port.inbound.RecordDailyLearningStatUseCase
import com.examhelper.api.learning_statistics.port.inbound.command.RecordDailyLearningStatCommand
import com.examhelper.api.learning_statistics.port.outbound.DailyLearningStatStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneOffset

@Service
class RecordDailyLearningStatService(
    private val dailyLearningStatStore: DailyLearningStatStore,
    private val idGenerator: IdGenerator
) : RecordDailyLearningStatUseCase {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun execute(command: RecordDailyLearningStatCommand) {
        val learningDate = command.submittedAt.atZone(ZoneOffset.UTC).toLocalDate()

        command.items.groupBy { it.subject }
            .forEach { (subjectName, items) ->
                val subject = Subject.valueOf(subjectName)

                val stat = dailyLearningStatStore.loadByMemberAndDateAndSubject(
                    memberId = MemberId(command.memberId),
                    date = learningDate,
                    subject = subject,
                ) ?: DailyLearningStat.create(
                    id = DailyLearningStatId(idGenerator.generateId()),
                    memberId = MemberId(command.memberId),
                    date = learningDate,
                    subject = subject,
                )

                items.forEach {
                    stat.accumulate(
                        isCorrect = it.isCorrect,
                        timeSpentSeconds = it.timeSpentSeconds,
                    )
                }

                dailyLearningStatStore.save(stat)
            }
    }
}
