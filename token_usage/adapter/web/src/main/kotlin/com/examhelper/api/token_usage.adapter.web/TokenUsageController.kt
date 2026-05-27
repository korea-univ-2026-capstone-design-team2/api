package com.examhelper.api.token_usage.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.token_usage.adapter.web.dto.request.RecordTokenUsageReqDto
import com.examhelper.api.token_usage.adapter.web.dto.response.RecordTokenUsageResDto
import com.examhelper.api.token_usage.port.inbound.RecordTokenUsageUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token-usages")
@Tag(
    name = "TokenUsage",
    description = "AI 토큰 사용량 기록 API"
)   
class TokenUsageController(
    private val recordTokenUsageUseCase: RecordTokenUsageUseCase,
) {
    @PostMapping
    @RecordTokenUsageDocs
    fun recordTokenUsage(
        @RequestBody request: RecordTokenUsageReqDto,
    ): ResponseEntity<ApiResponse.Success<RecordTokenUsageResDto>> {
        val result = recordTokenUsageUseCase.execute(request.toCommand())

        return ResponseEntity
            .status(HttpStatus.CREATED).body(
                ApiResponse.Success(
                    RecordTokenUsageResDto.fromResult(result)
                )
            )
    }
}
