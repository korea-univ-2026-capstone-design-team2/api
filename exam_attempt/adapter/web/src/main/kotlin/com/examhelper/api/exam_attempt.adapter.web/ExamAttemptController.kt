package com.examhelper.api.exam_attempt.adapter.web

import com.examhelper.api.exam_attempt.adapter.web.dto.request.SaveExamAttemptAnswersReqDto
import com.examhelper.api.exam_attempt.adapter.web.dto.request.StartExamAttemptReqDto
import com.examhelper.api.exam_attempt.adapter.web.dto.request.SubmitExamAttemptReqDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.ExamAttemptResDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.SaveExamAttemptAnswersResDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.StartExamAttemptResDto
import com.examhelper.api.exam_attempt.adapter.web.dto.response.SubmitExamAttemptResDto
import com.examhelper.api.exam_attempt.port.inbound.GetExamAttemptResultUseCase
import com.examhelper.api.exam_attempt.port.inbound.SaveExamAttemptAnswersUseCase
import com.examhelper.api.exam_attempt.port.inbound.StartExamAttemptUseCase
import com.examhelper.api.exam_attempt.port.inbound.SubmitExamAttemptUseCase
import com.examhelper.api.exam_attempt.port.inbound.query.GetExamAttemptResultQuery
import com.examhelper.api.infrastructure.web.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exam-attempts")
@Tag(name = "ExamAttempt", description = "시험 응시 API")
class ExamAttemptController(
    private val startExamAttemptUseCase: StartExamAttemptUseCase,
    private val saveExamAttemptAnswersUseCase: SaveExamAttemptAnswersUseCase,
    private val submitExamAttemptUseCase: SubmitExamAttemptUseCase,
    private val getExamAttemptResultUseCase: GetExamAttemptResultUseCase
) {
    @PostMapping
    @StartExamAttemptDocs
    fun startAttempt(
        // @AuthMember memberId: Long,
        @RequestBody reqDto: StartExamAttemptReqDto
    ): ResponseEntity<ApiResponse.Success<StartExamAttemptResDto>> {
        val result = startExamAttemptUseCase.execute(reqDto.toCommand())

        return ResponseEntity.ok(ApiResponse.Success(StartExamAttemptResDto.fromResult(result)))
    }

    @PatchMapping("/{attemptId}/answers")
    @SaveExamAttemptAnswersDocs
    fun saveAnswers(
//        @AuthMember memberId: Long,
        @PathVariable attemptId: String,
        @RequestBody reqDto: SaveExamAttemptAnswersReqDto
    ): ResponseEntity<ApiResponse.Success<SaveExamAttemptAnswersResDto>> {
        val result = saveExamAttemptAnswersUseCase.execute(reqDto.toCommand(attemptId, 1L.toString()))

        return ResponseEntity.ok(ApiResponse.Success(SaveExamAttemptAnswersResDto.fromResult(result)))
    }

    @PostMapping("/{attemptId}/submit")
    @SubmitExamAttemptDocs
    fun submit(
        //@CurrentMember member: MemberPrincipal,
        @PathVariable attemptId: String,
        @RequestBody request: SubmitExamAttemptReqDto,
    ): ResponseEntity<ApiResponse.Success<SubmitExamAttemptResDto>> {
        val result = submitExamAttemptUseCase.execute(
            request.toCommand(
                attemptId = attemptId,
                memberId = 1L.toString()
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(SubmitExamAttemptResDto.from(result)))
    }


    @GetMapping("/{attemptId}/result")
    @GetExamAttemptResultDocs
    fun getResult(
        //@AuthenticationPrincipal member: MemberPrincipal,
        @PathVariable attemptId: String,
    ): ResponseEntity<ApiResponse.Success<ExamAttemptResDto>> {
        val result = getExamAttemptResultUseCase.execute(
            GetExamAttemptResultQuery(
                attemptId = attemptId.toLong(),
                memberId = 1L
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(ExamAttemptResDto.fromView(result)))
    }
}
