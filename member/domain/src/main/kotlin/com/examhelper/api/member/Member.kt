package com.examhelper.api.member

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.AuthType
import java.time.Instant

class Member private constructor(
    id: MemberId,
    nickname: String,
    email: String,
    authType: AuthType,
    val createdAt: Instant,
    updatedAt: Instant
) : AggregateRoot<MemberId>(id) {
    var nickname: String = nickname
        private set
    var email: String = email
        private set
    var authType: AuthType = authType
        private set
    var updatedAt: Instant = updatedAt
        private set
    /* 이름 바꾸기 기능 */
    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
        this.updatedAt= Instant.now()
    }
    /* 팩토리 */
    companion object {
        fun create(
            id: MemberId,
            nickname: String,
            email: String,
            authType: AuthType
        ): Member {
            val now=Instant.now()
            return Member(
                id = id,
                nickname = nickname,
                email = email,
                authType = authType,
                createdAt = now,
                updatedAt = now
            )
        }
        fun of(
            id: MemberId,
            nickname: String,
            email: String,
            authType: AuthType,
            createdAt: Instant,
            updatedAt: Instant
        ): Member = Member(
            id = id,
            nickname = nickname,
            email = email,
            authType = authType,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}