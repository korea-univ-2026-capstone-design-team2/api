package com.examhelper.api.learning_statistics.port.inbound

import com.examhelper.api.learning_statistics.port.inbound.command.RecordDailyLearningStatCommand

interface RecordDailyLearningStatUseCase {
    fun execute(command: RecordDailyLearningStatCommand)
}
