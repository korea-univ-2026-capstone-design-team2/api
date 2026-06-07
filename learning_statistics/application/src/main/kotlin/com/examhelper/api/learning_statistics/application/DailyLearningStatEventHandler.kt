package com.examhelper.api.learning_statistics.application

import com.examhelper.api.kernel.event.ExamAttemptSubmittedEvent
import com.examhelper.api.learning_statistics.port.inbound.RecordDailyLearningStatUseCase
import com.examhelper.api.learning_statistics.port.inbound.command.RecordDailyLearningStatCommand
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class DailyLearningStatEventHandler(
    private val recordDailyLearningStatUseCase: RecordDailyLearningStatUseCase,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: ExamAttemptSubmittedEvent) {
        recordDailyLearningStatUseCase.execute(
            RecordDailyLearningStatCommand(
                memberId = event.memberId,
                submittedAt = event.submittedAt,
                items = event.items
            )
        )
    }
}
