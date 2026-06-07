package com.examhelper.api.auth.adapter.jwt

import com.examhelper.api.auth.port.outbound.JwtTokenProviderPort
import com.examhelper.api.auth.port.outbound.JwtTokenSet
import com.examhelper.api.kernel.identifier.MemberId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import io.jsonwebtoken.security.Keys

@Repository // 🟢 Spring Bean으로 등록하여 Service에 주입되게 합니다.
class JwtTokenProviderAdapter(
    // application.yml에서 비밀키와 만료시간을 가져옵니다.
    @Value("\${jwt.secret}") private val secretString: String,
    @Value("\${jwt.access-token-validity-in-ms}") private val accessTokenValidityInMs: Long,
    @Value("\${jwt.refresh-token-validity-in-ms}") private val refreshTokenValidityInMs: Long
) : JwtTokenProviderPort { // 🟢 우리가 정의한 포트를 상속받습니다!

    // 비밀키 세팅 (HMAC-SHA 알고리즘 사용)
    private val key = Keys.hmacShaKeyFor(secretString.toByteArray())

    override fun generateTokens(memberId: MemberId): JwtTokenSet {
        val now = Date()
        val accessValidity = Date(now.time + accessTokenValidityInMs)
        val refreshValidity = Date(now.time + refreshTokenValidityInMs)

        // 1. Access Token 생성 (회원 ID 포함)
        val accessToken = Jwts.builder()
            .setSubject(memberId.value) // 토큰의 주인을 MemberId로 설정
            .setIssuedAt(now)
            .setExpiration(accessValidity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        // 2. Refresh Token 생성 (보안상 페이로드 최소화)
        val refreshToken = Jwts.builder()
            .setSubject(memberId.value)
            .setIssuedAt(now)
            .setExpiration(refreshValidity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return JwtTokenSet(accessToken, refreshToken)
    }
}