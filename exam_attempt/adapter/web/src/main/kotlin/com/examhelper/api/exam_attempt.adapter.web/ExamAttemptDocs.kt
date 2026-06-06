package com.examhelper.api.exam_attempt.adapter.web

import com.examhelper.api.exam_attempt.adapter.web.dto.response.SaveExamAttemptAnswersResDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.StartExamAttemptResDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.SubmitExamAttemptResDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 응시 시작",
    description = """
        지정한 모의고사에 대한 응시(ExamAttempt)를 시작합니다.
        
        ### 동작
        
        - 새로운 응시 세션을 생성합니다.
        - 응시 시작 시간(`startedAt`)은 서버에서 기록됩니다.
        - 동일 회원이 동일 모의고사에 대해 진행 중(IN_PROGRESS)인 응시가 이미 존재하면 생성할 수 없습니다.
        
        ### 응답
        
        생성된 응시 세션 ID(`attemptId`)와 응시 상태를 반환합니다.
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "응시 시작 성공",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = StartExamAttemptResDto::class)
        )]
    ),
    ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 모의고사",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "409",
        description = "이미 진행 중인 응시가 존재함",
        content = [Content(schema = Schema(hidden = true))]
    )
)
annotation class StartExamAttemptDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 답안 저장",
    description = """
        진행 중인 모의고사 응시의 답안을 저장합니다.
        
        ### 사용 목적
        
        - 자동 저장(Autosave)
        - 문제 이동 시 저장
        - 답안 수정 시 저장
        - 북마크/모름 체크 상태 저장
        
        ### 동작
        
        - 동일한 questionItemId가 이미 저장되어 있으면 기존 답안을 갱신합니다.
        - 저장되지 않은 questionItemId인 경우 새 답안을 생성합니다.
        - 제출(SUBMITTED)된 응시는 수정할 수 없습니다.
        
        ### 저장 항목
        
        - 선택 답안(selectedNumber)
        - 문제 풀이 시간(timeSpentSeconds)
        - 모름 표시(markedUnknown)
        - 북마크(bookmarked)
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "답안 저장 성공",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = SaveExamAttemptAnswersResDto::class)
        )]
    ),
    ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 응시",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "403",
        description = "본인의 응시가 아님",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "409",
        description = "이미 제출된 응시는 수정할 수 없음",
        content = [Content(schema = Schema(hidden = true))]
    )
)
annotation class SaveExamAttemptAnswersDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "모의고사 제출",
    description = """
        진행 중인 모의고사 응시를 제출합니다.
        
        ### 동작
        
        - 전달된 답안을 최종 저장합니다.
        - 응시 상태를 SUBMITTED로 변경합니다.
        - 제출된 응시는 더 이상 수정할 수 없습니다.
        
        ### 참고
        
        채점 결과는 별도의 결과 조회 API를 통해 확인할 수 있습니다.
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "제출 성공",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = SubmitExamAttemptResDto::class)
        )]
    ),
    ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 응시",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "403",
        description = "본인의 응시가 아님",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "409",
        description = "이미 제출된 응시",
        content = [Content(schema = Schema(hidden = true))]
    )
)
annotation class SubmitExamAttemptDocs

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "시험 채점 결과 조회",
    description = """
        제출된 시험 응시의 채점 결과를 조회합니다.
        
        응답에는 다음 정보가 포함됩니다.
        
        - 총 문항 수
        - 정답 수
        - 점수
        - 정답률
        - 총 풀이 시간
        - 문항별 정오답 결과
        
        제출되지 않은 응시는 조회할 수 없습니다.
    """
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "채점 결과 조회 성공"
    ),
    ApiResponse(
        responseCode = "403",
        description = "본인의 응시 결과가 아님",
        content = [Content(schema = Schema(hidden = true))]
    ),
    ApiResponse(
        responseCode = "404",
        description = "응시를 찾을 수 없음",
        content = [Content(schema = Schema(hidden = true))]
    )
)
annotation class GetExamAttemptResultDocs
