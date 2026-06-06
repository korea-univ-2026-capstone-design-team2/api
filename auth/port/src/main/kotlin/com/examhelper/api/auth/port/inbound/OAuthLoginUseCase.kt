package com.examhelper.api.auth.port.inbound

// [UseCase] Controller가 바라보는 인터페이스 (구현체는 Application 계층에 위치)
interface OAuthLoginUseCase {
    fun execute(command: OAuthLoginCommand): OAuthLoginResult
}