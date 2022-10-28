package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authTokenService: AuthTokenService
) {
    @PostMapping("/api/v1/signup")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest
    ): AuthToken {
        return authTokenService.signUp(signUpRequest)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(
        @RequestBody loginRequest: LoginRequest
    ): AuthToken {
        return authTokenService.login(loginRequest)
    }

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe() {
        TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
    }
}