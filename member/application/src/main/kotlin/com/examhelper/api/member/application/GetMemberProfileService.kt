package com.examhelper.api.member.application

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.member.port.inbound.GetMemberProfileUseCase
import com.examhelper.api.member.port.inbound.command.GetMemberProfileCommand
import com.examhelper.api.member.port.inbound.result.MemberProfileResult
import com.examhelper.api.member.port.outbound.MemberStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberProfileService(
    private val memberStore: MemberStore
) : GetMemberProfileUseCase {

    @Transactional(readOnly = true) // 💡 데이터 변경이 없으므로 readOnly 최적화!
    override fun execute(command: GetMemberProfileCommand): MemberProfileResult {

        // 1. DB에서 회원 조회
        val memberId = MemberId(command.memberId)
        val member = memberStore.loadById(memberId)
            ?: throw IllegalArgumentException("해당 유저를 찾을 수 없습니다.")

        // 2. 결과 반환
        return MemberProfileResult(
            id = member.id.value,
            nickname = member.nickname,
            email = member.email,
            authType = member.authType.name
        )
    }
}