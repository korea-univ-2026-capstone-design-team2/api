package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.type.PropositionLabel

data class LlmPropositionResult(
    val label   : PropositionLabel,
    val content : String,
)
