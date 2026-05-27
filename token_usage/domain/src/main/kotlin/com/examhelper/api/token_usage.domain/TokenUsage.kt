package com.examhelper.api.token_usage.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.TokenUsageId
import com.examhelper.api.token_usage.domain.event.TokenUsageRecordedEvent
import com.examhelper.api.token_usage.domain.exception.TokenUsageAssertionException
import com.examhelper.api.token_usage.domain.exception.TokenUsageException
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import com.examhelper.api.token_usage.domain.vo.TokenConsumption
import com.examhelper.api.token_usage.domain.vo.TokenCost
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import java.time.Instant

class TokenUsage private constructor(
    id: TokenUsageId,
    // val memberId: MemberId?,
    val target: TokenUsageTarget,
    val provider: AiProvider,
    val model: AiModel,
    val tokenConsumption: TokenConsumption,
    val tokenCost: TokenCost,
    status: TokenUsageStatus,
    val createdAt: Instant
) : AggregateRoot<TokenUsageId>(id) {
    var status: TokenUsageStatus = status
        private set

    fun markFailed() {
        check(status != TokenUsageStatus.FAILED) {
            throw TokenUsageException.AlreadyFailed()
        }

        status = TokenUsageStatus.FAILED
    }

    private fun validate() {
        require(model.provider == provider) {
            throw TokenUsageAssertionException.ProviderMismatch(
                provider = provider,
                model = model,
            )
        }
    }

    companion object {
        fun create(
            id: TokenUsageId,
            //memberId: MemberId?,
            target: TokenUsageTarget,
            provider: AiProvider,
            model: AiModel,
            tokenConsumption: TokenConsumption,
            tokenCost: TokenCost,
        ): TokenUsage {
            val now = Instant.now()

            return TokenUsage(
                id = id,
                // memberId = memberId,
                target = target,
                provider = provider,
                model = model,
                tokenConsumption = tokenConsumption,
                tokenCost = tokenCost,
                status = TokenUsageStatus.SUCCESS,
                createdAt = now,
            ).also {
                it.addDomainEvent(
                    TokenUsageRecordedEvent(
                        tokenUsageId = id.value,
                        //memberId = memberId?.value,
                        provider = provider.name,
                        model = model.name,
                        targetType = target::class.simpleName!!,
                        targetReferenceId = target.referenceId,
                        promptTokens = tokenConsumption.promptTokens,
                        completionTokens = tokenConsumption.completionTokens,
                        totalTokens = tokenConsumption.totalTokens,
                        totalCost = tokenCost.totalCost,
                        currency = tokenCost.currency.currencyCode,
                        status = TokenUsageStatus.SUCCESS.name,
                        occurredAt = now
                    )
                )
            }
        }

        fun of(
            id: TokenUsageId,
            //memberId: MemberId?,
            target: TokenUsageTarget,
            provider: AiProvider,
            model: AiModel,
            tokenConsumption: TokenConsumption,
            tokenCost: TokenCost,
            status: TokenUsageStatus,
            createdAt: Instant
        ): TokenUsage {
            return TokenUsage(
                id = id,
                target = target,
                provider = provider,
                model = model,
                tokenConsumption = tokenConsumption,
                tokenCost = tokenCost,
                status = status,
                createdAt = createdAt
            )
        }
    }
}
