package com.examhelper.api.member.adapter.persistence

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.AuthType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.member.Member
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
){
    companion object {
        fun fromDomain(domain: Member): MemberEntity = MemberEntity(
            id=domain.id.value,
            nickname=domain.nickname,
            email=domain.email,
            authType=domain.authType,
            createdAt=domain.createdAt,
            updatedAt=domain.updatedAt
        )
    }
    /* this.id, this.nickname 처럼 this를 붙여야 한다? */
    fun toDomain(): Member = Member.of(
        id = MemberId(id),
        nickname = nickname,
        email = email,
        authType = authType,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}