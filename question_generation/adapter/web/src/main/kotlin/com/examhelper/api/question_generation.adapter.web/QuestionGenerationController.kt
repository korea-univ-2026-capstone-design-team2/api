package com.examhelper.api.question_generation.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.question_generation.adapter.web.request.GenerateQuestionReqDto
import com.examhelper.api.question_generation.adapter.web.request.IngestFrameReqDto
import com.examhelper.api.question_generation.adapter.web.response.GenerateQuestionResDto
import com.examhelper.api.question_generation.port.inbound.GenerateQuestionUseCase
import com.examhelper.api.question_generation.port.inbound.IngestFrameUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/question-generations")
class QuestionGenerationController(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val ingestFrameUseCase: IngestFrameUseCase
) {
    // TODO: 코루틴 전환 시 suspend fun + 201 Created 로 변경
    @PostMapping
    @GenerateQuestionDocs
    fun generate(
        @RequestBody @Valid request: GenerateQuestionReqDto,
    ): ResponseEntity<ApiResponse.Success<GenerateQuestionResDto>> {
        val result = generateQuestionUseCase.execute(request.toCommand())
        val data = ApiResponse.Success(GenerateQuestionResDto(
            generationId = result.questionGenerationId.value
        ))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }

    @PostMapping("/ingest")
    fun ingest(
        @RequestBody request: IngestFrameReqDto
    ): ResponseEntity<ApiResponse.Success<Unit>> {
        ingestFrameUseCase.execute(request.toCommand())
        val data = ApiResponse.Success(Unit)

        return ResponseEntity.status(HttpStatus.OK).body(data)
    }
}
