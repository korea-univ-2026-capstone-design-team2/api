package com.examhelper.api.auth.adapter.jwt

import com.examhelper.api.auth.port.outbound.JwtTokenProviderPort
import com.examhelper.api.auth.port.outbound.JwtTokenSet
import com.examhelper.api.kernel.identifier.MemberId
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureAlgorithm
import java.util.Date
import javax.crypto.SecretKey

@Repository // 🟢 Spring Bean으로 등록하여 Service에 주입되게 합니다.
class JwtTokenProviderAdapter(
    @Value("\${jwt.secret}") private val secretString: String,
    @Value("\${jwt.access-token-validity-in-ms}") private val accessTokenValidityInMs: Long,
    @Value("\${jwt.refresh-token-validity-in-ms}") private val refreshTokenValidityInMs: Long
) : JwtTokenProviderPort {

    // 💡 1. Key 생성: UTF-8 인코딩을 명시하여 안전하게 SecretKey를 생성합니다.
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray(Charsets.UTF_8))

    override fun generateTokens(memberId: MemberId): JwtTokenSet {
        val now = Date()
        val accessValidity = Date(now.time + accessTokenValidityInMs)
        val refreshValidity = Date(now.time + refreshTokenValidityInMs)

        // 💡 2. 최신 빌더 패턴: 'set'이 모두 사라지고 코드가 직관적으로 변했습니다.
        val accessToken = Jwts.builder()
            .subject(memberId.value.toString())     // setSubject -> subject
            .issuedAt(now)               // setIssuedAt -> issuedAt
            .expiration(accessValidity)  // setExpiration -> expiration
            .signWith(key)               // 💡 3. 알고리즘(HS256) 자동 추론
            .compact()

        val refreshToken = Jwts.builder()
            .subject(memberId.value.toString())
            .issuedAt(now)
            .expiration(refreshValidity)
            .signWith(key)               // 여기도 key만 넘기면 끝!
            .compact()

        return JwtTokenSet(accessToken, refreshToken)
    }
    override fun validateAndExtractMemberId(token: String): MemberId? {
        return try {
            // 최신 jjwt 문법인 parser()와 verifyWith()를 사용합니다.
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload

            // 토큰 주인의 ID(String)를 꺼내서 MemberId(Long) 객체로 복원합니다.
            val memberIdString = claims.subject
            MemberId(memberIdString.toLong())
        } catch (e: JwtException) {
            // 토큰이 변조되었거나, 만료되었을 경우 세부 예외가 터지며, null을 반환해 인증 실패 처리합니다.
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}