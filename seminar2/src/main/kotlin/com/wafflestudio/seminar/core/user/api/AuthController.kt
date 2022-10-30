package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    val userService: UserService
) {

    @PostMapping("/api/v1/signup")
    fun signUp(
        /* TODO: how to validate? */ @RequestBody @Valid signUpRequest: SignUpRequest
    ): AuthTokenResponse {
        return userService.signUp(signUpRequest)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(
        @RequestBody @Valid logInRequest: LogInRequest
    ): AuthTokenResponse {
        return userService.logIn(logInRequest)
    }

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @UserContext userid: Long
    ): UserInfoResponse {
        return userService.getUserById(userid)
    }

}