package com.examhelper.api.exam.adapter.web

import com.examhelper.api.exam.adapter.web.dto.response.GenerateExamResDto
import com.examhelper.api.exam.adapter.web.dto.response.GetExamDetailResDto
import com.examhelper.api.exam.adapter.web.dto.response.GetExamListResDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 생성",
    description = """
        RAG 기반으로 5급 PSAT 유형의 모의고사를 생성합니다.
        - 과거 10년 기출 데이터에서 논리 프레임 추출
        - 입력된 주제를 주입하여 신규 문항 생성
        - 생성 완료 시 즉시 응시 가능 (READY 상태)
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "201",
        description = "모의고사 생성 성공",
        content = [Content(schema = Schema(implementation = GenerateExamResDto::class))],
    ),
    ApiResponse(
        responseCode = "400",
        description = "유효하지 않은 요청 파라미터",
        content = [Content(schema = Schema(hidden = true))],
    ),
    ApiResponse(
        responseCode = "503",
        description = "AI 서비스 일시 불가",
        content = [Content(schema = Schema(hidden = true))],
    ),
)
annotation class GenerateExamDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 목록 조회",
    description = "과목/유형/난이도/상태 필터로 모의고사 목록을 페이징 조회합니다.",
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(schema = Schema(implementation = GetExamListResDto::class))],
    ),
)
annotation class GetExamListDocs


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 단건 조회",
    description = "examId로 모의고사 상세 정보와 문항 목록을 조회합니다.",
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(schema = Schema(implementation = GetExamDetailResDto::class))],
    ),
    ApiResponse(
        responseCode = "404",
        description = "시험을 찾을 수 없음",
        content = [Content(schema = Schema(hidden = true))],
    ),
)
annotation class GetExamDetailDocs
