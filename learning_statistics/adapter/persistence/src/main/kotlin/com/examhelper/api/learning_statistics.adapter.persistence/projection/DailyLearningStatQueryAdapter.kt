package com.examhelper.api.learning_statistics.adapter.persistence.projection

import com.examhelper.api.learning_statistics.adapter.persistence.projection.projection.DailyLearningRecordProjection
import com.examhelper.api.learning_statistics.adapter.persistence.projection.projection.DailyLearningSummaryProjection
import com.examhelper.api.learning_statistics.port.inbound.view.DailyLearningRecordView
import com.examhelper.api.learning_statistics.port.inbound.view.DailyLearningSummaryView
import com.examhelper.api.learning_statistics.port.outbound.DailyLearningStatReader
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DailyLearningStatQueryAdapter(
    private val jpaReader: DailyLearningStatJpaReader
) : DailyLearningStatReader {
    override fun findSummary(
        memberId: Long,
        from: LocalDate,
        to: LocalDate,
    ): DailyLearningSummaryView? =
        jpaReader.findSummary(memberId, from, to)?.toView()

    override fun findDailyRecords(
        memberId: Long,
        from: LocalDate,
        to: LocalDate,
    ): List<DailyLearningRecordView> =
        jpaReader.findDailyRecords(memberId, from, to).map { it.toView() }
}

private fun DailyLearningSummaryProjection.toView() =
    DailyLearningSummaryView(
        totalQuestions = totalQuestions,
        totalCorrect = totalCorrect,
        totalStudySeconds = totalStudySeconds,
    )

private fun DailyLearningRecordProjection.toView() =
    DailyLearningRecordView(
        date = date,
        questionCount = questionCount,
        correctCount = correctCount,
        studySeconds = studySeconds,
    )
