package com.examhelper.api.learning_statistics.application

import com.examhelper.api.learning_statistics.port.inbound.GetDailyLearningRecordsUseCase
import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningRecordsQuery
import com.examhelper.api.learning_statistics.port.outbound.DailyLearningStatReader
import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningRecordResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetDailyLearningRecordsService(
    private val dailyLearningStatReader: DailyLearningStatReader
) : GetDailyLearningRecordsUseCase {
    @Transactional(readOnly = true)
    override fun execute(query: GetDailyLearningRecordsQuery): List<DailyLearningRecordResult> =
        dailyLearningStatReader.findDailyRecords(
            memberId = query.memberId,
            from = query.from,
            to = query.to,
        ).map {
            DailyLearningRecordResult(
                date = it.date,
                questionCount = it.questionCount,
                correctCount = it.correctCount,
                studySeconds = it.studySeconds,
                accuracy =
                    if (it.questionCount == 0L) 0.0
                    else it.correctCount.toDouble() / it.questionCount,
            )
        }
}
