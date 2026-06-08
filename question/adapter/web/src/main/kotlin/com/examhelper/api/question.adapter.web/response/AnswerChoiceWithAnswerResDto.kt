package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.view.AnswerChoiceViewWithAnswer

class AnswerChoiceWithAnswerResDto(
    val number: Int,
    val text: String,
    val isCorrect: Boolean,
) {
    companion object {
        fun fromView(view: AnswerChoiceViewWithAnswer): AnswerChoiceWithAnswerResDto =
            AnswerChoiceWithAnswerResDto(
                number = view.number,
                text = view.text,
                isCorrect = view.isCorrect
            )
    }
}
