package com.examhelper.api.learning_statistics.adapter.web.dto

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.learning_statistics.adapter.web.dto.response.GetDailyLearningRecordsResDto
import com.examhelper.api.learning_statistics.adapter.web.dto.response.GetDailyLearningSummaryResDto
import com.examhelper.api.learning_statistics.port.inbound.GetDailyLearningRecordsUseCase
import com.examhelper.api.learning_statistics.port.inbound.GetDailyLearningSummaryUseCase
import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningRecordsQuery
import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningSummaryQuery
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "학습 통계 API")
class AnalyticsController(
    private val getDailyLearningSummaryUseCase: GetDailyLearningSummaryUseCase,
    private val getDailyLearningRecordsUseCase: GetDailyLearningRecordsUseCase
) {
    @GetMapping("/summary")
    @GetDailyLearningSummaryDocs
    fun getSummary(
        @RequestParam from: LocalDate,
        @RequestParam to: LocalDate,
    ): ResponseEntity<ApiResponse.Success<GetDailyLearningSummaryResDto>> {
        val result = getDailyLearningSummaryUseCase.execute(
            GetDailyLearningSummaryQuery(
                memberId = 1L,
                from = from,
                to = to,
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(GetDailyLearningSummaryResDto.from(result)))
    }

    @GetMapping("/daily-records")
    @GetDailyLearningRecordsDocs
    fun getDailyRecords(
        @RequestParam from: LocalDate,
        @RequestParam to: LocalDate
    ): ResponseEntity<ApiResponse.Success<GetDailyLearningRecordsResDto>> {
        val result = getDailyLearningRecordsUseCase.execute(
                GetDailyLearningRecordsQuery(
                    memberId = 1L,
                    from = from,
                    to = to,
                )
            )

        return ResponseEntity.ok(ApiResponse.Success(GetDailyLearningRecordsResDto.from(result)))
    }
}
