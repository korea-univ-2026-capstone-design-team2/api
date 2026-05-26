package com.examhelper.api.token_usage.adapter.persistence

import com.examhelper.api.kernel.identifier.TokenUsageId
import com.examhelper.api.token_usage.domain.TokenUsage
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import com.examhelper.api.token_usage.domain.vo.TokenConsumption
import com.examhelper.api.token_usage.domain.vo.TokenCost
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency

@Entity
@Table(
    name = "token_usages",
    indexes = [
        Index(name = "idx_token_usage_member_id", columnList = "memberId"),
        Index(name = "idx_token_usage_target", columnList = "targetType,targetReferenceId"),
        Index(name = "idx_token_usage_model", columnList = "model"),
        Index(name = "idx_token_usage_created_at", columnList = "createdAt"),
    ]
)
class TokenUsageEntity(
    @Id
    val id: Long,

    // ── Usage Target ─────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val targetDomain: TokenUsageDomain,

    @Column(nullable = false, updatable = false)
    val targetReferenceId: Long,

    // ── AI 정보 ───────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: AiProvider,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val model: AiModel,

    // ── Token 정보 ───────────────────────────────────────
    @Column(nullable = false)
    val promptTokens: Int,

    @Column(nullable = false)
    val completionTokens: Int,

    @Column(nullable = false)
    val totalTokens: Int,

    // ── 비용 정보 ────────────────────────────────────────
    @Column(nullable = false, precision = 18, scale = 8)
    val promptCost: BigDecimal,

    @Column(nullable = false, precision = 18, scale = 8)
    val completionCost: BigDecimal,

    @Column(nullable = false, precision = 18, scale = 8)
    val totalCost: BigDecimal,

    @Column(nullable = false, length = 10)
    val currency: String,

    // ── 상태 ─────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: TokenUsageStatus,

    // ── 타임스탬프 ───────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant
) {
    companion object {
        fun fromDomain(domain: TokenUsage): TokenUsageEntity =
            TokenUsageEntity(
                id = domain.id.value,
                targetDomain = domain.target.domain,
                targetReferenceId = domain.target.referenceId,
                provider = domain.provider,
                model = domain.model,
                promptTokens = domain.tokenConsumption.promptTokens,
                completionTokens = domain.tokenConsumption.completionTokens,
                totalTokens = domain.tokenConsumption.totalTokens,
                promptCost = domain.tokenCost.promptCost,
                completionCost = domain.tokenCost.completionCost,
                totalCost = domain.tokenCost.totalCost,
                currency = domain.tokenCost.currency.currencyCode,
                status = domain.status,
                createdAt = domain.createdAt
            )
    }

    fun toDomain(): TokenUsage =
        TokenUsage.of(
            id = TokenUsageId(id),
            target = TokenUsageTarget(
                domain = targetDomain,
                referenceId = targetReferenceId,
            ),
            provider = provider,
            model = model,
            tokenConsumption = TokenConsumption(
                promptTokens = promptTokens,
                completionTokens = completionTokens,
                totalTokens = totalTokens,
            ),
            tokenCost = TokenCost(
                promptCost = promptCost,
                completionCost = completionCost,
                totalCost = totalCost,
                currency = Currency.getInstance(currency),
            ),
            status = status,
            createdAt = createdAt
        )
}
