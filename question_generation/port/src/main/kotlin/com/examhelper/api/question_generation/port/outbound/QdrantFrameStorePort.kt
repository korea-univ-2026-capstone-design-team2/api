package com.examhelper.api.question_generation.port.outbound

interface QdrantFrameStorePort {
    fun save(points: List<IndexedFramePoint>)
}
