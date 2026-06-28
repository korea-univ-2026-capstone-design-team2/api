package com.examhelper.api.exam.adapter.messaging

import com.examhelper.api.exam.adapter.messaging.message.QuestionGeneratedMessage
import com.examhelper.api.exam.domain.exception.ExamAssertionException
import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.port.inbound.AddExamItemUseCase
import com.examhelper.api.exam.port.inbound.command.AddExamItemCommand
import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionGeneratedEventListener(
    private val addExamItemUseCase: AddExamItemUseCase,
    private val deserializer: KafkaMessageDeserializer,
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["psat.question-generation.question-generated"],
        groupId = "examhelper-consumer-group",
    )
    @Transactional
    fun handle(
        record: ConsumerRecord<String, String>,
        ack: Acknowledgment,
    ) {
        runCatching {
            val message = deserializer.deserialize(record.value(), QuestionGeneratedMessage::class.java)

            addExamItemUseCase.execute(message.toCommand())

            ack.acknowledge()
        }.onFailure { ex ->
            when (ex) {
                is ExamAssertionException, is ExamException -> {
                    logger.error(ex) {
                        "Permanent failure(QuestionGeneratedEvent), skipping retry: key=${record.key()}"
                    }
                    ack.acknowledge()
                }

                is CannotAcquireLockException, is PessimisticLockingFailureException -> {
                    logger.warn(ex) {
                        "락 획득 타임아웃, 재시도 예정: key=${record.key()}"
                    }
                }

                else -> {
                    logger.error(ex) { "Failed(QuestionGeneratedEvent), will retry: key=${record.key()}" }
                }
            }
        }
    }

    private fun QuestionGeneratedMessage.toCommand() =
        AddExamItemCommand(
            generationId = QuestionGenerationId(generationId),
            questionId = QuestionId(questionId),
            ordering = ordering,
        )
}
