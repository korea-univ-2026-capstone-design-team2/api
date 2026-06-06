package com.examhelper.api.member.adapter.persistence

import com.examhelper.api.member.Member
import com.examhelper.api.member.port.outbound.MemberStore
import org.springframework.stereotype.Repository

@Repository
class MemberStoreAdapter(
    private val jpaStore: MemberJpaStore
): MemberStore {
    override fun save(member: Member): Member {
        // 도메인 -> 엔티티 번역
        val entity = MemberEntity.fromDomain(member)
        // DB 저장
        val savedEntity = jpaStore.save(entity)
        // 엔티티 -> 도메인 번역 후 반환
        return savedEntity.toDomain()
    }
}