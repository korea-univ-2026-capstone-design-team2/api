package com.examhelper.api.question_generation.adapter.web

import com.examhelper.api.question_generation.adapter.web.response.GenerateQuestionResDto
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
        
        **생성 전략**
        - 과거 10년 기출 데이터에서 논리 프레임 추출 (Vector Similarity Search)
        - 입력된 주제를 프레임에 주입하여 신규 문항 생성
        
        **자동 랜덤 선택** (값을 입력하지 않으면 자동 결정)
        - `subject` → 과목 랜덤 선택
        - `questionType` → 문제 유형 랜덤 선택
        - `questionSubType` → 선택된 questionType에 호환되는 하위 유형 중 랜덤 선택
        - `difficulty` → 난이도 랜덤 선택
        - `topicCategory` → 선택된 subject의 기본 주제 목록 중 랜덤 선택
        
        **허용값**
        - `subject`: VERBAL_LOGIC, DATA_INTERPRETATION, SITUATIONAL_JUDGMENT
        - `questionType`: READING, LOGIC_PUZZLE, ARGUMENTATION
        - `questionSubType`: MATCH, KNOWABLE, CONTEXT_CORRECTION, BLANK_FILLING, CORE_ARGUMENT, INFERENCE, ARGUMENT_ANALYSIS, STRENGTHEN_WEAKEN
        - `difficulty`: EASY, MEDIUM, HARD
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "201",
        description = "문제 생성 요청 수락",
        content = [Content(schema = Schema(implementation = GenerateQuestionResDto::class))]
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
annotation class GenerateQuestionDocs
