package com.examhelper.api.learning_statistics.adapter.messaging

import com.examhelper.api.infrastructure.message.KafkaMessageDeserializer
import com.examhelper.api.kernel.event.ExamAttemptSubmittedItem
import com.examhelper.api.learning_statistics.adapter.messaging.message.ExamAttemptSubmittedItemMessage
import com.examhelper.api.learning_statistics.adapter.messaging.message.ExamAttemptSubmittedMessage
import com.examhelper.api.learning_statistics.port.inbound.RecordDailyLearningStatUseCase
import com.examhelper.api.learning_statistics.port.inbound.command.RecordDailyLearningStatCommand
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ExamAttemptSubmittedEventListener(
    private val deserializer: KafkaMessageDeserializer,
    private val recordDailyLearningStatUseCase: RecordDailyLearningStatUseCase,
) {
    private val log = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["psat.exam-attempt.submitted"],
        groupId = "examhelper-consumer-group",
    )
    @Transactional
    fun handle(record: ConsumerRecord<String, String>) {
        runCatching {
            val message = deserializer.deserialize(
                record.value(),
                ExamAttemptSubmittedMessage::class.java,
            )
            recordDailyLearningStatUseCase.execute(message.toCommand())

        }.onFailure { ex ->
            log.error(ex) { "Failed: topic=${record.topic()}, key=${record.key()}" }
            throw ex
        }
    }

    private fun ExamAttemptSubmittedMessage.toCommand() =
        RecordDailyLearningStatCommand(
            memberId = memberId,
            submittedAt = submittedAt,
            items = items.map { it.toCommandItem() },
        )

    private fun ExamAttemptSubmittedItemMessage.toCommandItem() =
        ExamAttemptSubmittedItem(
            questionItemId = questionItemId,
            subject = subject,
            isCorrect = isCorrect,
            timeSpentSeconds = timeSpentSeconds
        )
}
