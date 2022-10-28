package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.*

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
    fun signIn(
        @RequestBody signInRequest: SignInRequest
    ): AuthToken {
        return authTokenService.signIn(signInRequest)
    }

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @RequestHeader("Authorization") authHeader: String,
        @UserContext userId: Long
    ): Long {
        return userId
    }
}