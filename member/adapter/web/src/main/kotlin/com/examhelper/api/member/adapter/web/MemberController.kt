package com.examhelper.api.member.adapter.web

import com.examhelper.api.infrastructure.web.ApiResponse
import com.examhelper.api.member.adapter.web.request.RegisterMemberReqDto
import com.examhelper.api.member.adapter.web.response.RegisterMemberResDto
import com.examhelper.api.member.port.inbound.RegisterMemberUseCase
import com.examhelper.api.member.port.inbound.command.RegisterMemberCommand
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
    private val registerMemberUseCase: RegisterMemberUseCase
) {
    @PostMapping("/register") // 멤버 등록하기
    fun registerMember(
        @RequestBody request: RegisterMemberReqDto
    ) : ResponseEntity<ApiResponse.Success<RegisterMemberResDto>> {
        // 3. 웹 DTO를 내부 비즈니스 레이어가 사용할 Command 객체로 변환합니다.
        val command: RegisterMemberCommand = request.toCommand()

        val result = registerMemberUseCase.execute(command)

        val data = ApiResponse.Success(RegisterMemberResDto.fromResult(result))

        return ResponseEntity.status(HttpStatus.CREATED).body(data)
    }
}