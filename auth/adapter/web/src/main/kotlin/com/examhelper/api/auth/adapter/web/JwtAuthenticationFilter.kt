package com.examhelper.api.auth.adapter.web

import com.examhelper.api.auth.port.outbound.JwtTokenProviderPort
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProviderPort: JwtTokenProviderPort
) : OncePerRequestFilter() { // 모든 요청마다 딱 한 번씩만 수행되는 시큐리티 필터

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. HTTP 헤더에서 Authorization 값을 꺼냅니다.
        val authorizationHeader = request.getHeader("Authorization")

        // 2. "Bearer <토큰>" 형태가 맞는지 확인합니다.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7) // "Bearer " 뒷부분의 실제 토큰만 추출

            // 3. 아웃바운드 포트를 사용해 토큰 검증 및 MemberId 획득
            val memberId = jwtTokenProviderPort.validateAndExtractMemberId(token)

            if (memberId != null) {
                // 4. 검증 성공 시, 스프링 시큐리티 시스템에 "이 사람 인증된 사용자야!"라고 명찰을 달아줍니다.
                // 💡 여기서 memberId.value(Long)를 넣었기 때문에 컨트롤러에서 @AuthenticationPrincipal로 바로 꺼낼 수 있습니다.
                val authentication = UsernamePasswordAuthenticationToken(
                    memberId.value, // Principal 객체로 Long ID 할당
                    null,
                    listOf() // 권한 목록 (현재는 비어둠)
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        // 5. 다음 문(필터 혹은 컨트롤러)으로 요청을 통과시킵니다.
        filterChain.doFilter(request, response)
    }
}