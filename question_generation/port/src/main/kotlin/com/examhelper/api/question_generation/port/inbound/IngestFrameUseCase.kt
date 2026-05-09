package com.examhelper.api.question_generation.port.inbound

import com.examhelper.api.question_generation.port.inbound.command.IngestFrameCommand

interface IngestFrameUseCase {
    fun execute(command: IngestFrameCommand)
}
