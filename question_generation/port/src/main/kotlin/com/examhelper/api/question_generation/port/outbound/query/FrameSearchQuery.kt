package com.examhelper.api.question_generation.port.outbound.query

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationTopic

data class FrameSearchQuery(
    val questionType    : QuestionType,
    val questionSubType : QuestionSubType?,
    val difficulty      : DifficultyLevel,
    val topic           : QuestionGenerationTopic,
    val topK            : Int
) {
    companion object {
        fun from(request: QuestionGenerationRequest): FrameSearchQuery =
            FrameSearchQuery(
                questionType    = request.questionType,
                questionSubType = request.questionSubType,
                difficulty      = request.difficulty,
                topic           = request.topic,
                topK            = request.frameSearchTopK
            )
    }
}
