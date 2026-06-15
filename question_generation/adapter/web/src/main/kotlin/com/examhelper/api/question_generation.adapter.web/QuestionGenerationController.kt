package com.examhelper.api.question_generation.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.question_generation.adapter.web.request.GenerateQuestionReqDto
import com.examhelper.api.question_generation.adapter.web.request.IngestFrameReqDto
import com.examhelper.api.question_generation.adapter.web.response.GenerateQuestionResDto
import com.examhelper.api.question_generation.port.inbound.GenerateQuestionUseCase
import com.examhelper.api.question_generation.port.inbound.IngestFrameUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/question-generations")
@Tag(name = "QuestionGeneration", description = "문제 생성 API")
class QuestionGenerationController(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val ingestFrameUseCase: IngestFrameUseCase,
    private val registry: QuestionGenerationEmitterRegistry
) {
    // TODO: 코루틴 전환 시 suspend fun + 201 Created 로 변경
    @PostMapping
    @GenerateQuestionDocs
    fun generate(
        @RequestBody @Valid request: GenerateQuestionReqDto,
        @AuthenticationPrincipal memberId: Long
    ): ResponseEntity<ApiResponse.Success<GenerateQuestionResDto>> {
        val result = generateQuestionUseCase.execute(request.toCommand(memberId))
        val data = ApiResponse.Success(GenerateQuestionResDto.fromResult(result))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }

    @GetMapping(
        "/{generationId}/events",
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun subscribe(
        @PathVariable generationId: String,
        @AuthenticationPrincipal memberId: Long
    ): SseEmitter {
        val emitter = registry.connect(generationId.toLong())

        registry.send(
            generationId = generationId.toLong(),
            eventName = "connected",
            data = "connected",
        )

        return emitter
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
