package com.examhelper.api.exam.adapter.web

import com.examhelper.api.exam.adapter.web.dto.request.GenerateExamReqDto
import com.examhelper.api.exam.adapter.web.dto.response.GenerateExamResDto
import com.examhelper.api.exam.adapter.web.dto.response.GetExamDetailResDto
import com.examhelper.api.exam.adapter.web.dto.response.GetExamListResDto
import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.port.inbound.GenerateExamUseCase
import com.examhelper.api.exam.port.inbound.GetExamDetailUseCase
import com.examhelper.api.exam.port.inbound.GetExamListUseCase
import com.examhelper.api.exam.port.inbound.query.ExamFilter
import com.examhelper.api.exam.port.inbound.query.GetExamDetailQuery
import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exams")
@Tag(name = "Exam", description = "PSAT 모의고사 생성 API")
class ExamController(
    private val generateExamUseCase: GenerateExamUseCase,
    private val getExamListUseCase: GetExamListUseCase,
    private val getExamDetailUseCase: GetExamDetailUseCase
) {
    // ── 생성 ──────────────────────────────────────────────
    @PostMapping
    @GenerateExamDocs
    fun generateExam(
        @RequestBody request: GenerateExamReqDto,
    ): ResponseEntity<ApiResponse.Success<GenerateExamResDto>> {
        val result = generateExamUseCase.execute(request.toCommand())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.Success(GenerateExamResDto.fromResult(result)))
    }

    // ── 단건 조회 ─────────────────────────────────────────

    @GetMapping("/{examId}")
    @GetExamDetailDocs
    fun getExamDetail(
        @PathVariable examId: Long,
    ): ResponseEntity<ApiResponse.Success<GetExamDetailResDto>> {
        val view = getExamDetailUseCase.execute(GetExamDetailQuery(examId))
        return ResponseEntity.ok(ApiResponse.Success(GetExamDetailResDto.fromView(view)))
    }

    // ── 목록 조회 ─────────────────────────────────────────
    @GetMapping
    @GetExamListDocs
    fun getExamList(
        @RequestParam(required = false) subject: Subject?,
        @RequestParam(required = false) questionType: QuestionType?,
        @RequestParam(required = false) difficulty: DifficultyLevel?,
        @RequestParam(required = false) status: ExamStatus?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<ApiResponse.Success<GetExamListResDto>> {
        val result = getExamListUseCase.execute(
            ExamFilter(
                subject      = subject,
                questionType = questionType,
                difficulty   = difficulty,
                status       = status,
                page         = page,
                size         = size,
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(GetExamListResDto.fromResult(result)))
    }
}
