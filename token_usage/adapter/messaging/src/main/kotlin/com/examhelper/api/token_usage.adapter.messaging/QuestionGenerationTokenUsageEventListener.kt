package com.examhelper.api.token_usage.adapter.messaging

import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.token_usage.adapter.messaging.message.QuestionGenerationTokenUsageMessage
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import com.examhelper.api.token_usage.port.inbound.RecordTokenUsageUseCase
import com.examhelper.api.token_usage.port.inbound.command.RecordTokenUsageCommand
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class QuestionGenerationTokenUsageEventListener(
    private val deserializer: KafkaMessageDeserializer,
    private val recordTokenUsageUseCase: RecordTokenUsageUseCase
) {
    private val log = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["psat.question-generation.token-used"],
        groupId = "examhelper-token-usage-group"
    )
    fun handle(record: ConsumerRecord<String, String>) {
        runCatching {
            val message = deserializer.deserialize(
                record.value(),
                QuestionGenerationTokenUsageMessage::class.java,
            )
            recordTokenUsageUseCase.execute(message.toCommand())

        }.onFailure { ex ->
            log.error(ex) { "Failed: topic=${record.topic()}, key=${record.key()}" }
            throw ex
        }
    }

    private fun QuestionGenerationTokenUsageMessage.toCommand() = RecordTokenUsageCommand(
        memberId = MemberId(memberId),
        target = TokenUsageTarget(
            domain = TokenUsageDomain.QUESTION_GENERATION,
            referenceId = generationId,
        ),
        model = AiModel.fromModelName(model),
        promptTokens = promptTokens,
        completionTokens = completionTokens,
        totalTokens = totalTokens
    )
}
