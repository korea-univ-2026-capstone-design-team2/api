package com.examhelper.api.question.adapter.web

import com.examhelper.api.question.adapter.web.response.AssignQualityScoreResDto
import com.examhelper.api.question.adapter.web.response.AssignQuestionToSetResDto
import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import com.examhelper.api.question.adapter.web.response.PublishQuestionResDto
import com.examhelper.api.question.adapter.web.response.RejectQuestionResDto
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
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

// ── 출제 ──────────────────────────────────────────────────

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제 출제",
    description = """
        DRAFT 상태의 문제를 PUBLISHED로 전환합니다.
        - 품질 점수(qualityScore)가 기준(0.75) 이상이어야 합니다.
        - 이미 출제된 문제는 다시 출제할 수 없습니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "출제 성공",
        content = [Content(schema = Schema(implementation = PublishQuestionResDto::class))],
    ),
    ApiResponse(responseCode = "400", description = "품질 점수 미달 또는 상태 전이 불가", content = [Content(schema = Schema(hidden = true))]),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class PublishQuestionDocs

// ── 반려 ──────────────────────────────────────────────────

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제 반려",
    description = """
        DRAFT 상태의 문제를 REJECTED로 전환합니다.
        - DRAFT 상태에서만 반려할 수 있습니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "반려 성공",
        content = [Content(schema = Schema(implementation = RejectQuestionResDto::class))],
    ),
    ApiResponse(responseCode = "400", description = "상태 전이 불가", content = [Content(schema = Schema(hidden = true))]),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class RejectQuestionDocs

// ── 품질 점수 부여 ─────────────────────────────────────────

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "품질 점수 부여",
    description = """
        품질 평가 AI 파이프라인이 산출한 점수를 문제에 반영합니다.
        - 점수 범위: 0.0 ~ 1.0
        - 0.75 이상이면 출제(Publish) 가능 상태가 됩니다.
        - 추후 이벤트 핸들러로 대체될 수 있습니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "점수 부여 성공",
        content = [Content(schema = Schema(implementation = AssignQualityScoreResDto::class))],
    ),
    ApiResponse(responseCode = "400", description = "유효하지 않은 점수 범위", content = [Content(schema = Schema(hidden = true))]),
    ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class AssignQualityScoreDocs

// ── 세트 편입 ──────────────────────────────────────────────

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "문제 세트 편입",
    description = """
        독립 문제를 문제 세트에 편입합니다.
        - 이미 세트에 편입된 문제는 다른 세트로 이동할 수 없습니다.
        - 세트당 최대 5개 문제까지 편입 가능합니다.
    """,
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "세트 편입 성공",
        content = [Content(schema = Schema(implementation = AssignQuestionToSetResDto::class))],
    ),
    ApiResponse(responseCode = "400", description = "세트 정원 초과 또는 이미 편입된 문제", content = [Content(schema = Schema(hidden = true))]),
    ApiResponse(responseCode = "404", description = "문제 또는 세트를 찾을 수 없음", content = [Content(schema = Schema(hidden = true))]),
)
annotation class AssignQuestionToSetDocs

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
