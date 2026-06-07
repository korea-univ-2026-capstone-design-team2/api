package com.examhelper.api.auth.application

import com.examhelper.api.auth.port.inbound.OAuthLoginUseCase
import com.examhelper.api.auth.port.inbound.command.OAuthLoginCommand
import com.examhelper.api.auth.port.inbound.result.OAuthLoginResult
import com.examhelper.api.auth.port.outbound.JwtTokenProviderPort
import com.examhelper.api.auth.port.outbound.OAuthClientPort
import com.examhelper.api.auth.port.outbound.OAuthCredentialStore
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.MemberId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthLoginService(
    // [아웃바운드 포트들] 동료가 만든 adapter 폴더들의 인터페이스들입니다!
    private val oauthClientPort: OAuthClientPort,           // oauth 폴더에서 구현
    private val oauthCredentialStore: OAuthCredentialStore, // persistence 폴더에서 구현
    private val memberStore: MemberStore,                   // persistence 폴더에서 구현
    private val jwtTokenProviderPort: JwtTokenProviderPort, // jwt 폴더에서 구현
    private val idGenerator: IdGenerator
) : OAuthLoginUseCase {

    @Transactional
    override fun execute(command: OAuthLoginCommand): OAuthLoginResult {

        // ── 1. OAuth 토큰 검증 및 유저 정보 획득 (oauth 어댑터 사용) ──
        // 구글 서버와 통신해서 진짜 유저 정보(프로필)를 가져옵니다.
        val oauthProfile = oauthClientPort.getUserProfile(command.provider, command.idToken)

        // ── 2. 기존 연동 정보 확인 (persistence 어댑터 사용) ────────
        var credential = oauthCredentialStore.findByProviderAndProviderId(
            provider = command.provider,
            providerId = oauthProfile.providerId
        )

        val memberId: MemberId

        // ── 3. 분기 처리 (신규 가입 vs 기존 로그인) ────────────────
        if (credential == null) {
            // [신규 가입]
            // 3-1. Member 도메인 생성 및 저장
            memberId = MemberId(idGenerator.generateId())
            val newMember = Member.create(
                id = memberId,
                nickname = oauthProfile.name ?: "Unknown", // 구글에서 받은 이름
                email = oauthProfile.email,
                authType = AuthType.OAUTH
            )
            memberStore.save(newMember)

            // 3-2. OAuthCredential 도메인 생성 및 저장 (구글 계정과 Member 연결)
            credential = OAuthCredential.create(
                id = CredentialId(idGenerator.generateId()),
                memberId = memberId,
                provider = command.provider,
                providerId = oauthProfile.providerId,
                email = oauthProfile.email
            )
            oauthCredentialStore.save(credential)

        } else {
            // [기존 로그인] 기존에 연동된 MemberId를 가져옵니다.
            memberId = credential.memberId
        }

        // ── 4. JWT 토큰 발급 (jwt 어댑터 사용) ───────────────────
        // 우리 서비스만의 로그인 증명서(Access/Refresh Token)를 만듭니다.
        val tokens = jwtTokenProviderPort.generateTokens(memberId)

        // ── 5. 결과 반환 ─────────────────────────────────────────
        return OAuthLoginResult(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )
    }
}