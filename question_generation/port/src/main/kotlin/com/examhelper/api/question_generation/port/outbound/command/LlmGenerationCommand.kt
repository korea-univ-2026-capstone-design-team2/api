package com.examhelper.api.question_generation.port.outbound.command

import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult

data class LlmGenerationCommand(
    val generationRequest : QuestionGenerationRequest,
    val referenceFrames   : List<FrameSearchResult>,
)
