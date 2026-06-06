package com.examhelper.api.member.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaStore: JpaRepository<MemberEntity, Long>