package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.view.QuestionItemPropositionView

data class QuestionItemPropositionResDto(
    val label: String,
    val content: String,
) {
    companion object {
        fun fromView(view: QuestionItemPropositionView): QuestionItemPropositionResDto =
            QuestionItemPropositionResDto(
                label = view.label,
                content = view.content
            )
    }
}