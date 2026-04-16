package com.examhelper.api.question.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.question.adapter.web.request.CreateQuestionReqDto
import com.examhelper.api.question.adapter.web.response.CreateQuestionResDto
import com.examhelper.api.question.port.`in`.CreateQuestionUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
class QuestionController(
    private val createQuestionUseCase: CreateQuestionUseCase
) {
    @PostMapping
    fun createQuestion(
        @RequestBody request: CreateQuestionReqDto
    ): ResponseEntity<ApiResponse.Success<CreateQuestionResDto>> {
        val command = request.toCommand()
        val result = createQuestionUseCase.execute(command)

        val data = ApiResponse.Success(CreateQuestionResDto.fromResult(result))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }
}
