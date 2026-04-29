package com.examhelper.api.question.adapter.persistence.record

import com.examhelper.api.kernel.identifier.LogicalFrameId
import com.examhelper.api.question.domain.vo.FrameReference

class FrameReferenceRecord (
    val frameId: Long,
    val similarityScore: Double,
    val frameType: String,
) {
    fun toDomain(): FrameReference = FrameReference(
        frameId = LogicalFrameId(frameId),
        similarityScore = similarityScore,
        frameType = frameType,
    )

    companion object {
        fun fromDomain(domain: FrameReference): FrameReferenceRecord = FrameReferenceRecord(
            frameId = domain.frameId.value,
            similarityScore = domain.similarityScore,
            frameType = domain.frameType,
        )
    }
}
