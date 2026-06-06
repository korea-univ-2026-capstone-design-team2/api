package com.examhelper.api.member.application
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.AuthType
import com.examhelper.api.member.Member
import com.examhelper.api.member.port.inbound.RegisterMemberUseCase
import com.examhelper.api.member.port.inbound.command.RegisterMemberCommand
import com.examhelper.api.member.port.inbound.result.RegisterMemberResult
import com.examhelper.api.member.port.outbound.MemberStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterMemberService(
    private val memberStore: MemberStore,
    private val idGenerator: IdGenerator
) : RegisterMemberUseCase {
    @Transactional
    override fun execute(
        command: RegisterMemberCommand
    ): RegisterMemberResult { // View 대신 Result 객체 반환으로 통일

        // ── 1. Aggregate 생성 ─────────────────────────────
        // 원시 타입(String/Long) 대신 도메인 값 객체(Value Object) 사용
        val memberId = MemberId(idGenerator.generateId())

        // DB 엔티티가 아닌 순수 도메인 모델의 팩토리 메서드(.create) 사용
        val member = Member.create(
            id = memberId,
            nickname = command.name,
            email = command.email,
            authType = AuthType.LOCAL // 현재는 로컬 로그인을 상정함.
        )
        memberStore.save(member)

        // ── 3. 결과 반환 ──────────────────────────────────
        return RegisterMemberResult(
            memberId = member.id.value,
            email = member.email
        )
    }
}