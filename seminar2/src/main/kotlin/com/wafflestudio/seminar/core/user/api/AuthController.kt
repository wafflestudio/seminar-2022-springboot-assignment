package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/api/v1/signup")
    fun signUp(
        @RequestBody @Valid req: UserDto.SignUpRequest
    ): AuthToken {
        return userService.signUp(req)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(
        @RequestHeader(value = "email") email: String,
        @RequestHeader(value = "password") password: String
    ): AuthToken {
        return userService.logIn(email, password)
    }

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext userId: Long
    ) : UserDto.UserResponse {
        return userService.getMe(userId)
    }

}