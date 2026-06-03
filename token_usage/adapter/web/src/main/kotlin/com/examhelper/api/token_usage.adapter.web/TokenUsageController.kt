package com.examhelper.api.token_usage.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.token_usage.adapter.web.dto.request.RecordTokenUsageReqDto
import com.examhelper.api.token_usage.adapter.web.dto.response.RecordTokenUsageResDto
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import com.examhelper.api.token_usage.port.inbound.GetTokenUsageListUseCase
import com.examhelper.api.token_usage.port.inbound.GetTokenUsageStatisticsUseCase
import com.examhelper.api.token_usage.port.inbound.RecordTokenUsageUseCase
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageFilter
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageStatisticsFilter
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageListResult
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageStatisticsResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/token-usages")
@Tag(name = "TokenUsage", description = "AI 토큰 사용량 기록 API")
class TokenUsageController(
    private val recordTokenUsageUseCase: RecordTokenUsageUseCase,
    private val getTokenUsageListUseCase: GetTokenUsageListUseCase,
    private val getTokenUsageStatisticsUseCase: GetTokenUsageStatisticsUseCase
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

    @GetMapping
    @GetTokenUsageListDocs
    fun getTokenUsages(
        @RequestParam(required = false) targetDomain: TokenUsageDomain?,
        @RequestParam(required = false) targetReferenceId: Long?,
        @RequestParam(required = false) provider: AiProvider?,
        @RequestParam(required = false) model: AiModel?,
        @RequestParam(required = false) status: TokenUsageStatus?,
        @RequestParam(required = false) from: Instant?,
        @RequestParam(required = false) to: Instant?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20")
        size: Int,
    ): ResponseEntity<ApiResponse.Success<GetTokenUsageListResult>> {
        val result = getTokenUsageListUseCase.execute(
            TokenUsageFilter(
                targetDomain = targetDomain,
                targetReferenceId = targetReferenceId,
                provider = provider,
                model = model,
                status = status,
                from = from,
                to = to,
                page = page,
                size = size
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(result))
    }

    // ── Statistics ──────────────────────────────────────
    @GetMapping("/statistics")
    @GetTokenUsageStatisticsDocs
    fun getStatistics(
        @RequestParam(required = false) targetDomain: TokenUsageDomain?,
        @RequestParam(required = false) targetReferenceId: Long?,
        @RequestParam(required = false) provider: AiProvider?,
        @RequestParam(required = false) model: AiModel?,
        @RequestParam(required = false) status: TokenUsageStatus?,
        @RequestParam(required = false) from: Instant?,
        @RequestParam(required = false) to: Instant?,
    ): ResponseEntity<ApiResponse.Success<GetTokenUsageStatisticsResult>> {
        val result = getTokenUsageStatisticsUseCase.execute(
            TokenUsageStatisticsFilter(
                targetDomain = targetDomain,
                targetReferenceId = targetReferenceId,
                provider = provider,
                model = model,
                status = status,
                from = from,
                to = to
            )
        )

        return ResponseEntity.ok(ApiResponse.Success(result))
    }
}
