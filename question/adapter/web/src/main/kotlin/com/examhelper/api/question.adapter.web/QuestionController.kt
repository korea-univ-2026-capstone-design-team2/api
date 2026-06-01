package com.examhelper.api.question.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.question.adapter.web.request.CreateQuestionReqDto
import com.examhelper.api.question.adapter.web.request.GetQuestionPapersReqDto
import com.examhelper.api.question.adapter.web.request.GetQuestionReviewsReqDto
import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.GetQuestionDetailUseCase
import com.examhelper.api.question.port.inbound.GetQuestionPaperUseCase
import com.examhelper.api.question.port.inbound.GetQuestionPapersUseCase
import com.examhelper.api.question.port.inbound.GetQuestionReviewUseCase
import com.examhelper.api.question.port.inbound.GetQuestionReviewsUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionDetailQuery
import com.examhelper.api.question.port.inbound.query.GetQuestionPaperQuery
import com.examhelper.api.question.port.inbound.query.GetQuestionPapersQuery
import com.examhelper.api.question.port.inbound.query.GetQuestionReviewQuery
import com.examhelper.api.question.port.inbound.query.GetQuestionReviewsQuery
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
    private val getQuestionPaperUseCase: GetQuestionPaperUseCase,
    private val getQuestionReviewUseCase: GetQuestionReviewUseCase,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val getQuestionPapersUseCase: GetQuestionPapersUseCase,
    private val getQuestionReviewsUseCase: GetQuestionReviewsUseCase
) {
    // ── 생성 ──────────────────────────────────────────────
    @PostMapping
    @CreateQuestionDocs
    fun createQuestion(
        @RequestBody request: CreateQuestionReqDto,
    ): ResponseEntity<ApiResponse.Success<CreateQuestionResDto>> {
        val result = createQuestionUseCase.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.Success(CreateQuestionResDto.fromResult(result)))
    }

    // ── 문제 풀이용 조회 ───────────────────────────────────
    @GetMapping("/{questionId}/paper")
    @GetQuestionPaperDocs
    fun getQuestionPaper(
        @PathVariable questionId: Long,
    ): ResponseEntity<ApiResponse.Success<QuestionPaperView>> {
        val view = getQuestionPaperUseCase.execute(GetQuestionPaperQuery(questionId))
        return ResponseEntity.ok(ApiResponse.Success(view))
    }

    // ── 해설지 조회 ────────────────────────────────────────
    @GetMapping("/{questionId}/review")
    @GetQuestionReviewDocs
    fun getQuestionReview(
        @PathVariable questionId: Long,
    ): ResponseEntity<ApiResponse.Success<QuestionReviewView>> {
        val view = getQuestionReviewUseCase.execute(GetQuestionReviewQuery(questionId))
        return ResponseEntity.ok(ApiResponse.Success(view))
    }

    // ── 관리용 상세 조회 ───────────────────────────────────
    @GetMapping("/{questionId}/detail")
    @GetQuestionDetailDocs
    fun getQuestionGroupDetail(
        @PathVariable questionId: Long,
    ): ResponseEntity<ApiResponse.Success<QuestionDetailView>> {
        val view = getQuestionDetailUseCase.execute(GetQuestionDetailQuery(questionId))
        return ResponseEntity.ok(ApiResponse.Success(view))
    }

    // ── 문제지 목록 조회 ───────────────────────────────────
    @PostMapping("/papers")
    @GetQuestionPapersDocs
    fun getQuestionPapers(
        @RequestBody request: GetQuestionPapersReqDto,
    ): ResponseEntity<ApiResponse.Success<List<QuestionPaperView>>> {

        val views = getQuestionPapersUseCase.execute(
            GetQuestionPapersQuery(request.questionIds)
        )

        return ResponseEntity.ok(ApiResponse.Success(views))
    }

    // ── 해설지 목록 조회 ──────────────────────────────────────
    @PostMapping("/reviews")
    @GetQuestionReviewsDocs
    fun getQuestionReviews(
        @RequestBody request: GetQuestionReviewsReqDto,
    ): ResponseEntity<ApiResponse.Success<List<QuestionReviewView>>> {

        val views = getQuestionReviewsUseCase.execute(
            GetQuestionReviewsQuery(request.questionIds)
        )

        return ResponseEntity.ok(ApiResponse.Success(views))
    }
/*
    // ── 목록 조회 ──────────────────────────────────────────
    @GetMapping
    fun getQuestionGroups(
        @ModelAttribute filter: QuestionGroupFilterReqDto,
    ): ResponseEntity<ApiResponse.Success<QuestionGroupListResDto>> {
        val result = getQuestionGroupListUseCase.execute(filter.toQuery())
        return ResponseEntity.ok(ApiResponse.Success(QuestionGroupListResDto.fromResult(result)))
    }

 */
}
