package com.examhelper.api.token_usage.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.TokenUsageId
import com.examhelper.api.token_usage.application.resolver.TokenPricingPolicyResolver
import com.examhelper.api.token_usage.domain.TokenUsage
import com.examhelper.api.token_usage.domain.vo.TokenConsumption
import com.examhelper.api.token_usage.port.inbound.RecordTokenUsageUseCase
import com.examhelper.api.token_usage.port.inbound.command.RecordTokenUsageCommand
import com.examhelper.api.token_usage.port.inbound.result.RecordTokenUsageResult
import com.examhelper.api.token_usage.port.outbound.TokenUsageStore
import org.springframework.stereotype.Service

@Service
class RecordTokenUsageService(
    private val tokenUsageStore: TokenUsageStore,
    private val tokenPricingPolicyResolver: TokenPricingPolicyResolver,
    private val idGenerator: IdGenerator
) : RecordTokenUsageUseCase {
    override fun execute(command: RecordTokenUsageCommand): RecordTokenUsageResult {
        val tokenConsumption = TokenConsumption(
            promptTokens = command.promptTokens,
            completionTokens = command.completionTokens,
            totalTokens = command.totalTokens,
        )

        val tokenCost = tokenPricingPolicyResolver
            .resolve(command.model.provider)
            .calculate(
                model = command.model,
                consumption = tokenConsumption,
            )

        val tokenUsage = TokenUsage.create(
            id = TokenUsageId(idGenerator.generateId()),
            memberId = command.memberId,
            target = command.target,
            provider = command.model.provider,
            model = command.model,
            tokenConsumption = tokenConsumption,
            tokenCost = tokenCost,
        )

        tokenUsageStore.save(tokenUsage)

        return RecordTokenUsageResult(tokenUsage.id.value)
    }
}
