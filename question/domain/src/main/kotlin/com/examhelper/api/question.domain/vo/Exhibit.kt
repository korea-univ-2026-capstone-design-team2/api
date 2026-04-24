package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

sealed class Exhibit {

    data class PropositionExhibit(
        val propositions: List<Proposition>,
    ) : Exhibit() {
        init { validate() }

        private fun validate() {
            require(propositions.size in 2..5) {
                throw QuestionAssertionException.PropositionSizeMismatch(propositions.size)
            }
            val labels = propositions.map { it.label }
            require(labels.distinct().size == labels.size) {
                throw QuestionAssertionException.PropositionLabelDuplicated()
            }
        }
    }

    data class TextExhibit(
        val content: String,
    ) : Exhibit() {
        init {
            require(content.isNotBlank()) {
                throw QuestionAssertionException.ExhibitContentBlank()
            }
        }
    }

    // 확장 시 추가 예정
    // data class TableExhibit(...) : Exhibit()
    // data class ImageExhibit(...) : Exhibit()
}
