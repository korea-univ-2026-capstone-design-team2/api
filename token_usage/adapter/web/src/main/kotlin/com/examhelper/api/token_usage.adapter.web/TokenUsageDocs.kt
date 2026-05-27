package com.examhelper.api.token_usage.adapter.web

import com.examhelper.api.token_usage.adapter.web.dto.response.RecordTokenUsageResDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "AI 토큰 사용량 기록(임시)",
    description = """
        AI 모델 호출에 대한 토큰 사용량 및 비용 정보를 기록합니다.
        
        - Prompt / Completion 토큰 수 저장
        - 모델별 비용 추적
        - AI 사용량 분석 및 정산 목적
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "201",
        description = "토큰 사용량 기록 성공",
        content = [
            Content(
                schema = Schema(
                    implementation = RecordTokenUsageResDto::class
                )
            )
        ]
    ),
    ApiResponse(
        responseCode = "400",
        description = "유효하지 않은 요청",
        content = [Content(schema = Schema(hidden = true))]
    ),
)
annotation class RecordTokenUsageDocs
