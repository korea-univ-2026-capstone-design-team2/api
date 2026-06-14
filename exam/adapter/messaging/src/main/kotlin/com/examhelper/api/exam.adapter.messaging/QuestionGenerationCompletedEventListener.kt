package com.examhelper.api.exam.adapter.messaging

import com.examhelper.api.exam.adapter.messaging.message.QuestionGenerationCompletedMessage
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.port.inbound.CompleteExamGenerationUseCase
import com.examhelper.api.exam.port.inbound.command.CompleteExamGenerationCommand
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.event.GenerationCompletedEvent
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Component
class QuestionGenerationCompletedEventListener(
    private val completeExamGenerationUseCase: CompleteExamGenerationUseCase,
    private val deserializer: KafkaMessageDeserializer,
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["psat.question-generation.completed"],
        groupId = "examhelper-consumer-group",
    )
    @Transactional
    fun handle(
        record: ConsumerRecord<String, String>,
        ack: Acknowledgment,
    ) {
        runCatching {
            val message = deserializer.deserialize(record.value(), QuestionGenerationCompletedMessage::class.java,)

            completeExamGenerationUseCase.execute(message.toCommand())

            ack.acknowledge()
        }.onFailure { logger.error("Failed(QuestionGenerationCompletedEvent): key=${record.key()}", it) }
    }

    private fun QuestionGenerationCompletedMessage.toCommand() =
        CompleteExamGenerationCommand(
            generationId = QuestionGenerationId(generationId),
            successCount = successCount,
            failCount = failureCount,
        )
}
