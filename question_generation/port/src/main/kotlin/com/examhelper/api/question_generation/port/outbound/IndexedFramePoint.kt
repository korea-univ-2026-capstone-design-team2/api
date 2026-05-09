package com.examhelper.api.question_generation.port.outbound

data class IndexedFramePoint(
    val id: String,
    val vector: List<Float>,
    val payload: Map<String, Any>
)
