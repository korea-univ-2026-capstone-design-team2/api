package com.examhelper.api.question.adapter.web

import com.examhelper.api.question.adapter.web.response.AssignQualityScoreResDto
import com.examhelper.api.question.adapter.web.response.AssignQuestionToSetResDto
import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "PSAT 문제 생성(로직 테스트용)",
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

// ── 문제지용 조회 ──────────────────────────────────────────
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제지 조회",
    description = """
        수험생에게 제공할 문제지 데이터를 반환합니다.
        - 정답(correctNumber, isCorrect)은 포함되지 않습니다.
        - stem, passage, exhibit, choices(정답 제외)를 포함합니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(schema = Schema(implementation = QuestionPaperView::class))],
    ),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class GetQuestionPaperDocs

// ── 풀이 확인용 조회 ───────────────────────────────────────
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "풀이 확인 조회",
    description = """
        풀이 확인에 필요한 전체 데이터를 반환합니다.
        - 정답(correctNumber, isCorrect)이 포함됩니다.
        - 정답 해설(correctReason) 및 오답 해설(incorrectReasons)이 포함됩니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(schema = Schema(implementation = QuestionReviewView::class))],
    ),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class GetQuestionReviewDocs

// ── 문제 상세 정보 조회(관리용) ───────────────────────────────────────
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제 상세 정보 조회(관리용)",
    description = """
        문제에 대한 전체 데이터를 반환합니다.
        - 모든 데이터를 반환합니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(schema = Schema(implementation = QuestionDetailView::class))],
    ),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class GetQuestionDetailDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제지 다건 조회",
    description = """
        여러 문제의 문제지 데이터를 반환합니다.
        
        - 정답(correctNumber, isCorrect)은 포함되지 않습니다.
        - stem, passage, exhibit, choices(정답 제외)를 포함합니다.
        - 시험지 생성 시 사용됩니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(array = ArraySchema(Schema(QuestionPaperView::class)))]
    ),
)
annotation class GetQuestionPapersDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "해설지 다건 조회",
    description = """
        여러 문제의 해설지 데이터를 반환합니다.
        
        - 정답(correctNumber)을 포함합니다.
        - 보기별 정오답 정보(isCorrect)를 포함합니다.
        - 정답 해설(correctReason)을 포함합니다.
        - 시험 결과 확인 시 사용됩니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(array = ArraySchema(Schema(QuestionReviewView::class)))],
    ),
)
annotation class GetQuestionReviewsDocs
