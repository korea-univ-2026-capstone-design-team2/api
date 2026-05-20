package com.examhelper.api.question.domain.vo

import com.examhelper.api.kernel.identifier.LogicalFrameId
import com.examhelper.api.question.domain.exception.QuestionItemAssertionException

data class FrameReference(
    val frameId: LogicalFrameId,
    val similarityScore: Double,
    val frameType: String,
) {
    init {
        require(similarityScore in 0.0..1.0) {
            throw QuestionItemAssertionException.SimilarityScoreOutOfRange(similarityScore)
        }
    }
}
