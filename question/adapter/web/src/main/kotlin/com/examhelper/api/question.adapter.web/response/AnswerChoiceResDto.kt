package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.view.AnswerChoiceView

data class AnswerChoiceResDto(
    val number: Int,
    val text: String
) {
    companion object {
        fun fromView(view: AnswerChoiceView): AnswerChoiceResDto =
            AnswerChoiceResDto(
                number = view.number,
                text = view.text
            )
    }
}
