package com.examhelper.api.question_generation.adapter.web.request

import com.examhelper.api.question_generation.adapter.web.frame.FrameDocumentDto
import com.examhelper.api.question_generation.port.inbound.command.IngestFrameCommand

data class IngestFrameReqDto(
    val frames: List<FrameDocumentDto>
) {
    fun toCommand(): IngestFrameCommand {
        return IngestFrameCommand(frames.map { it.toDocument() })
    }
}
