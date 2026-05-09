package com.examhelper.api.question_generation.port.inbound.command

import com.examhelper.api.question_generation.port.inbound.model.LogicalFrameDocument

data class IngestFrameCommand(
    val frames: List<LogicalFrameDocument>
)
