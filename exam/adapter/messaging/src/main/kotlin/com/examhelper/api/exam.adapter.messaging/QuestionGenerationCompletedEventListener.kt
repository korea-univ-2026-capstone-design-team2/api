package com.examhelper.api.exam.adapter.messaging

import com.examhelper.api.exam.adapter.messaging.message.QuestionGenerationCompletedMessage
import com.examhelper.api.exam.domain.exception.ExamAssertionException
import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.port.inbound.CompleteExamGenerationUseCase
import com.examhelper.api.exam.port.inbound.command.CompleteExamGenerationCommand
import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

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
            val message = deserializer.deserialize(record.value(), QuestionGenerationCompletedMessage::class.java)

            completeExamGenerationUseCase.execute(message.toCommand())

            ack.acknowledge()
        }.onFailure { ex ->
            when (ex) {
                is ExamAssertionException -> {
                    logger.error(ex) {
                        "Permanent failure(QuestionGenerationCompletedEvent), skipping retry: key=${record.key()}"
                    }
                    ack.acknowledge()
                }
                is ExamException -> {
                    logger.error(ex) {
                        "Exam not found(QuestionGenerationCompletedEvent), skipping retry: key=${record.key()}"
                    }
                    ack.acknowledge()
                }
                else -> {
                    logger.error(ex) { "Failed(QuestionGenerationCompletedEvent), will retry: key=${record.key()}" }
                }
            }
        }
    }

    private fun QuestionGenerationCompletedMessage.toCommand() =
        CompleteExamGenerationCommand(
            generationId = QuestionGenerationId(generationId),
            successCount = successCount,
            failCount = failureCount,
        )
}
