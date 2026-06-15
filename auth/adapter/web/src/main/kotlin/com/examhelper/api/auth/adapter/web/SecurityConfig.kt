package com.examhelper.api.auth.adapter.web

import com.examhelper.api.auth.port.outbound.JwtTokenProviderPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity // 🟢 스프링 시큐리티의 웹 보안 기능을 활성화합니다.
class SecurityConfig(
    // 필터에 주입해 줄 포트를 가져옵니다.
    private val jwtTokenProviderPort: JwtTokenProviderPort
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        // 1. 우리가 만든 문지기 객체 생성
        val jwtFilter = JwtAuthenticationFilter(jwtTokenProviderPort)

        http
            // 2. REST API 전용 설정 (불필요한 기본 기능 끄기)
            .cors {  }
            .csrf { it.disable() }        // 브라우저 폼 로그인이 아니므로 CSRF 비활성화
            .httpBasic { it.disable() }   // 헤더에 ID/PW를 달고 다니는 기본 방식 비활성화
            .formLogin { it.disable() }   // 스프링 기본 로그인 화면 비활성화

            // 3. 세션 정책 설정 (가장 중요!)
            .sessionManagement {
                // 서버에 세션을 저장하지 않고(STATELESS), 오직 JWT 토큰으로만 인증하겠다고 선언!
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // 4. 출입국 관리소 라우팅 설정
            .authorizeHttpRequests { auth ->
                // 로그인이나 최초 회원가입 API는 토큰이 없어도 패스! (누구나 접근 가능)
                auth.requestMatchers("/auth/**").permitAll()
                auth.requestMatchers("/swagger-ui/**").permitAll()
                auth.requestMatchers("/v3/api-docs/**").permitAll()
                auth.requestMatchers("/favicon.ico").permitAll()

                // 💡 그 외의 모든 요청(특히 /members/me)은 무조건 인증(토큰)을 거쳐야 한다!
                auth.anyRequest().authenticated()
            }

            // 5. ⭐️ 문지기 배치: 우리의 JWT 필터를 스프링의 기본 인증 필터 '앞에' 세웁니다.
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
