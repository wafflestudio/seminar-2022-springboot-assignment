package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authTokenService: AuthTokenService
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid signUpRequest: SignUpRequest
    ): AuthToken {
        return authTokenService.signUp(signUpRequest)
    }

    @PostMapping("/signin")
    fun signIn(
        @RequestBody signInRequest: SignInRequest
    ): AuthToken {
        return authTokenService.signIn(signInRequest)
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(
        @RequestHeader("Authorization") authHeader: String,
        @UserContext userId: Long
    ): Long {
        return userId
    }
}