package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody request: SignUpRequest): AuthToken {
        return userService.createUser(request)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody request: SignInRequest): AuthToken {
        val (email, password) = request
        return userService.logIn(email, password)
    }

}