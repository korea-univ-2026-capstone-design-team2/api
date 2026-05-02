package com.examhelper.api.question.domain.vo

import com.examhelper.api.kernel.type.PropositionLabel

data class Proposition(
    val label: PropositionLabel,
    val content: String,
)
