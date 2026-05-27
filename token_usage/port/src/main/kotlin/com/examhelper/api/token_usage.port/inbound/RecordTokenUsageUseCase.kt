package com.examhelper.api.token_usage.port.inbound

import com.examhelper.api.token_usage.port.inbound.command.RecordTokenUsageCommand
import com.examhelper.api.token_usage.port.inbound.result.RecordTokenUsageResult

interface RecordTokenUsageUseCase {
    fun execute(command: RecordTokenUsageCommand): RecordTokenUsageResult
}
