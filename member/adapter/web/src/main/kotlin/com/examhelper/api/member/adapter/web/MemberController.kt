package com.examhelper.api.member.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.member.adapter.web.request.RegisterMemberReqDto
import com.examhelper.api.member.adapter.web.response.MemberProfileResDto
import com.examhelper.api.member.adapter.web.response.RegisterMemberResDto
import com.examhelper.api.member.port.inbound.GetMemberProfileUseCase
import com.examhelper.api.member.port.inbound.RegisterMemberUseCase
import com.examhelper.api.member.port.inbound.command.GetMemberProfileCommand
import com.examhelper.api.member.port.inbound.command.RegisterMemberCommand
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
    //private val registerMemberUseCase: RegisterMemberUseCase
    private val getMemberProfileUseCase: GetMemberProfileUseCase
) {
    /*@PostMapping("/register") // 멤버 등록하기
    fun registerMember(
        @RequestBody request: RegisterMemberReqDto
    ) : ResponseEntity<ApiResponse.Success<RegisterMemberResDto>> {
        // 3. 웹 DTO를 내부 비즈니스 레이어가 사용할 Command 객체로 변환합니다.
        val command: RegisterMemberCommand = request.toCommand()

        val result = registerMemberUseCase.execute(command)

        val data = ApiResponse.Success(RegisterMemberResDto.fromResult(result))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }*/
    @GetMapping("/me")
    fun getMyProfile(
        // 💡 핵심: 프론트엔드가 보낸 JWT 토큰을 까서, 시큐리티가 알아서 이 변수에 ID를 넣어줍니다.
        @AuthenticationPrincipal memberId: Long
    ): ResponseEntity<ApiResponse.Success<MemberProfileResDto>> {

        // 1. 커맨드 생성
        val command = GetMemberProfileCommand(memberId)

        // 2. 유스케이스 실행 (DB에서 Member 조회)
        val result = getMemberProfileUseCase.execute(command)

        // 3. 결과 포장 및 반환
        val data = ApiResponse.Success(MemberProfileResDto.from(result))
        return ResponseEntity.ok(data)
    }
}