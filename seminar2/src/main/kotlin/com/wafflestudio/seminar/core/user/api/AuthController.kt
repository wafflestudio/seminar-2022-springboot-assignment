package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody request: SignUpRequest): User  {
        return userService.createUser(request)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody request: SignInRequest): AuthToken {
        return userService.loginUser(request)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe() {
        // TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
    }
    
}