package com.examhelper.api.member.port.outbound

import com.examhelper.api.member.Member

interface MemberStore {
    fun save(member: Member): Member
}