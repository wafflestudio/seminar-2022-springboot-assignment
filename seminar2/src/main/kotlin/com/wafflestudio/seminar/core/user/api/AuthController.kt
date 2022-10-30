package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody request: SignUpRequest): AuthToken  {
        return userService.createUser(request)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody request: SignInRequest): AuthToken {
        return userService.loginUser(request)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@RequestAttribute userId: Long): User {
        return userService.getUser(userId)
    }
}