package com.examhelper.api.learning_statistics.adapter.web.dto

import com.examhelper.api.learning_statistics.adapter.web.dto.response.GetDailyLearningRecordsResDto
import com.examhelper.api.learning_statistics.adapter.web.dto.response.GetDailyLearningSummaryResDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "학습 통계 요약 조회",
    description = """
        지정한 기간 동안의 학습 통계 요약 정보를 조회합니다.
        
        ### 포함 정보
        
        - 총 풀이 문제 수
        - 총 정답 수
        - 정답률
        - 총 학습 시간(초)
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = GetDailyLearningSummaryResDto::class)
        )]
    )
)
annotation class GetDailyLearningSummaryDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "일별 학습 기록 조회",
    description = """
        지정한 기간 동안의 일별 학습 기록을 조회합니다.
        
        ### 포함 정보
        
        - 날짜
        - 풀이 문제 수
        - 정답 수
        - 정답률
        - 학습 시간(초)
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = GetDailyLearningRecordsResDto::class)
        )]
    )
)
annotation class GetDailyLearningRecordsDocs
