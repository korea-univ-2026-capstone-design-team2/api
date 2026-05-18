package com.examhelper.api.member

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.AuthType
import java.time.Instant

class Member private constructor(
    id: MemberId,
    nickname: String,
    email: String,
    authType: AuthType, // 로그인 타입. ENUM 으로 하는게 좋을까요?
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
}