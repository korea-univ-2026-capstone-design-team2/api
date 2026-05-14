package com.examhelper.api.member

import com.examhelper.api.kernel.identifier.MemberId
import java.time.Instant

class Member private constructor(
    id: MemberId,
    nickname: String,
    email: String,
    authType: String, // 로그인 타입. ENUM 으로 하는게 좋을까요? 
    val createdAt: Instant,
    updatedAt: Instant
    ){
    // 모듈 구조 유지용
}