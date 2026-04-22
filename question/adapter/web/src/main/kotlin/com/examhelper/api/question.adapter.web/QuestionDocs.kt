package com.examhelper.api.question.adapter.web

import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "PSAT 문제 생성",
    description = """
        RAG 기반으로 5급 PSAT 유형의 문제를 생성합니다.
        - 과거 10년 기출 데이터에서 논리 프레임 추출
        - 입력된 주제를 주입하여 신규 문항 생성
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "201",
        description = "문제 생성 요청 수락 (비동기 처리)",
        content = [Content(schema = Schema(implementation = CreateQuestionResDto::class))]
    ),
    ApiResponse(responseCode = "400", description = "유효하지 않은 요청 파라미터", content = [Content(schema = Schema(hidden = true))]),
    ApiResponse(responseCode = "503", description = "AI 서비스 일시 불가", content = [Content(schema = Schema(hidden = true))]),
)
annotation class CreateQuestionDocs
