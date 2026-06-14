package com.examhelper.api.exam.adapter.messaging

import com.examhelper.api.exam.adapter.messaging.message.QuestionGeneratedMessage
import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.port.inbound.AddExamItemUseCase
import com.examhelper.api.exam.port.inbound.command.AddExamItemCommand
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.ExamItemId
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionGeneratedEventListener(
    private val addExamItemUseCase: AddExamItemUseCase,
    private val deserializer: KafkaMessageDeserializer
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["psat.question-generation.question-generated"],
        groupId = "examhelper-consumer-group",
    )
    @Transactional
    fun handle(
        record: ConsumerRecord<String, String>,
        ack: Acknowledgment
    ) {
        runCatching {
            val message = deserializer.deserialize(record.value(), QuestionGeneratedMessage::class.java)

            addExamItemUseCase.addItem(message.toAddExamItemCommand())
            ack.acknowledge()
        }.onFailure { logger.error("Failed(QuestionGeneratedEvent): key=${record.key()}", it) }
    }

    private fun QuestionGeneratedMessage.toAddExamItemCommand() = AddExamItemCommand(
        generationId = QuestionGenerationId(generationId),
        questionId   = QuestionId(questionId),
        ordering     = ordering
    )
}
