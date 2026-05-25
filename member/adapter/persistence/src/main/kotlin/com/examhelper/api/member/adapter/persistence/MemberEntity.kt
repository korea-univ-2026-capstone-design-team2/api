package com.examhelper.api.member.adapter.persistence

import com.examhelper.api.kernel.type.AuthType
import com.examhelper.api.kernel.type.QuestionType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


@Entity
@Table(name = "members")
class MemberEntity(
    @Id
    var id : Long, // id

    @Column(nullable = false)
    val nickname: String, // 닉네임

    @Column(nullable = false, updatable = false)
    val email: String, // 이메일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val authType: AuthType, // 로그인 방식

    // ── 타임스탬프 ─────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
){ // 이 괄호 안에는 무엇을 넣어야 하는가?

}