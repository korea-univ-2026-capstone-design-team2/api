package com.examhelper.api.auth.adapter.web

import com.examhelper.api.auth.adapter.web.request.OAuthLoginReqDto
import com.examhelper.api.auth.adapter.web.response.TokenResDto
import com.examhelper.api.auth.port.inbound.OAuthLoginUseCase
import com.examhelper.api.infrastructure.web.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class OAuthController(
    // 구체적인 Service 클래스가 아닌 인터페이스(UseCase)에 의존합니다!
    private val oauthLoginUseCase: OAuthLoginUseCase
) {

    @PostMapping("/oauth/login")
    fun oauthLogin(
        @RequestBody request: OAuthLoginReqDto
    ): ResponseEntity<ApiResponse.Success<TokenResDto>> {

        // 1. 웹의 언어(DTO)를 비즈니스의 언어(Command)로 번역
        val command = request.toCommand()

        // 2. 핵심 비즈니스 로직 실행 (토큰 검증 -> 회원가입/로그인 -> JWT 발급)
        val result = oauthLoginUseCase.execute(command)

        // 3. 비즈니스의 결과(Result)를 다시 웹의 언어(DTO)로 포장
        val data = ApiResponse.Success(TokenResDto.from(result))

        // 4. 200 OK와 함께 클라이언트에게 토큰 발급
        return ResponseEntity.ok(data)
    }
}