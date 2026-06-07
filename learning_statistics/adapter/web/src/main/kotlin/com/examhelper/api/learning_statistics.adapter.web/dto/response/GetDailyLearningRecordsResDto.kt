package com.examhelper.api.learning_statistics.adapter.web.dto.response

import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningRecordResult

data class GetDailyLearningRecordsResDto(
    val items: List<DailyLearningRecordDto>,
) {
    companion object {
        fun from(results: List<DailyLearningRecordResult>) = GetDailyLearningRecordsResDto(
            items = results.map {
                DailyLearningRecordDto(
                    date = it.date,
                    questionCount = it.questionCount,
                    correctCount = it.correctCount,
                    studySeconds = it.studySeconds,
                    accuracy = it.accuracy,
                )
            }
        )
    }
}
