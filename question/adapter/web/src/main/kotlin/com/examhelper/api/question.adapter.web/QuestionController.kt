package com.examhelper.api.question.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.question.adapter.web.request.CreateQuestionReqDto
import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import com.examhelper.api.question.adapter.web.response.PublishQuestionResDto
import com.examhelper.api.question.adapter.web.response.RejectQuestionResDto
import com.examhelper.api.question.port.inbound.AssignQualityScoreUseCase
import com.examhelper.api.question.port.inbound.AssignQuestionToSetUseCase
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.GetQuestionPaperUseCase
import com.examhelper.api.question.port.inbound.GetQuestionReviewUseCase
import com.examhelper.api.question.port.inbound.PublishQuestionUseCase
import com.examhelper.api.question.port.inbound.RejectQuestionUseCase
import com.examhelper.api.question.port.inbound.command.PublishQuestionCommand
import com.examhelper.api.question.port.inbound.command.RejectQuestionCommand
import com.examhelper.api.question.port.inbound.query.GetQuestionPaperQuery
import com.examhelper.api.question.port.inbound.query.GetQuestionReviewQuery
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller는 HTTP 요청을 처리하는 REST 컨트롤러입니다.
 * @RestController 어노테이션은 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
 * @RequestMapping("/questions") 어노테이션은 이 컨트롤러가 "/subscription" 경로로 들어오는 요청을 처리하도록 지정합니다.
 * createQuestion 메서드는 POST 요청을 처리하며, CreateQuestionReqDto 객체를 요청 본문에서 받아 CreateQuestionUseCase를 통해 질문을 생성합니다.
 * 생성된 질문의 결과는 CreateQuestionResDto로 변환되어 HTTP 응답으로 반환됩니다.
 */
@RestController
@RequestMapping("/questions")
@Tag(name = "Question", description = "PSAT 문제 생성 API")
class QuestionController(
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val publishQuestionUseCase: PublishQuestionUseCase,
    private val rejectQuestionUseCase: RejectQuestionUseCase,
    private val getQuestionPaperUseCase: GetQuestionPaperUseCase,
    private val getQuestionReviewUseCase: GetQuestionReviewUseCase
) {
    @PostMapping
    @CreateQuestionDocs
    fun createQuestion(
        @RequestBody request: CreateQuestionReqDto
    ): ResponseEntity<ApiResponse.Success<CreateQuestionResDto>> {
        val command = request.toCommand()
        val result = createQuestionUseCase.execute(command)

        val data = ApiResponse.Success(CreateQuestionResDto.fromResult(result))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }

    // ── 출제 ──────────────────────────────────────────────────
    @PatchMapping("/{questionId}/publish")
    @PublishQuestionDocs
    fun publishQuestion(
        @PathVariable questionId: Long,
    ): ResponseEntity<ApiResponse.Success<PublishQuestionResDto>> {
        val result = publishQuestionUseCase.execute(PublishQuestionCommand(questionId))
        return ResponseEntity.ok(ApiResponse.Success(PublishQuestionResDto.fromResult(result)))
    }

    // ── 반려 ──────────────────────────────────────────────────
    @PatchMapping("/{questionId}/reject")
    @RejectQuestionDocs
    fun rejectQuestion(
        @PathVariable questionId: Long
    ): ResponseEntity<ApiResponse.Success<RejectQuestionResDto>> {
        val result = rejectQuestionUseCase.execute(RejectQuestionCommand(questionId))
        return ResponseEntity.ok(ApiResponse.Success(RejectQuestionResDto.fromResult(result)))
    }

    // ── 문제 조회 ───────────────────────────────────────────────
    @GetMapping("/{questionId}/paper")
    @GetQuestionPaperDocs
    fun getQuestionForPaper(
        @PathVariable questionId: Long
    ): ResponseEntity<ApiResponse.Success<QuestionPaperView>> {
        val view = getQuestionPaperUseCase.execute(GetQuestionPaperQuery(questionId))
        return ResponseEntity.ok(ApiResponse.Success(view))
    }

    // ── 답지 조회 ───────────────────────────────────────────────
    @GetMapping("/{questionId}/review")
    @GetQuestionReviewDocs
    fun getQuestionForReview(
        @PathVariable questionId: Long,
    ): ResponseEntity<ApiResponse.Success<QuestionReviewView>> {
        val view = getQuestionReviewUseCase.execute(GetQuestionReviewQuery(questionId))
        return ResponseEntity.ok(ApiResponse.Success(view))
    }
}
