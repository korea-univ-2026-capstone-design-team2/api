
package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionSetId
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Passage
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "question_sets")
class QuestionSetEntity(
    @Id
    val id: Long,
    // ── JSON 컬럼 ──────────────────────────────────────────
    // Passage와 Exhibit을 재사용하기 위해 QuestionContentConverter 내부 변환 로직 활용
    // QuestionSet 전용 Converter를 따로 만들지 않고, nullable JSON 컬럼으로 처리
    @Column(columnDefinition = "JSON")
    val sharedPassage: String?,     // Passage? → JSON String

    @Column(columnDefinition = "JSON")
    val sharedExhibit: String?,     // Exhibit? → JSON String

    // ── 타임스탬프 ─────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
) {
    /*
    companion object {
        fun fromDomain(
            domain: QuestionSet,
            passageJson: String?,
            exhibitJson: String?,
        ): QuestionSetEntity = QuestionSetEntity(
            id = domain.id.value,
            sharedPassage = passageJson,
            sharedExhibit = exhibitJson,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomain(
        questions: List<QuestionId>,
        passage: Passage?,
        exhibit: Exhibit?,
    ): QuestionSet = QuestionSet.of(
        id = QuestionSetId(id),
        sharedPassage = passage,
        sharedExhibit = exhibit,
        questions = questions,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
    */
}