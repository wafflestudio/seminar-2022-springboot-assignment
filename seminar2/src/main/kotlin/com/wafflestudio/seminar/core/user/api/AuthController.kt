package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
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
    fun signUp(@RequestBody request: SignUpRequest): AuthToken {
        val (email, username, password) = request
        return userService.createUser(email, username, password)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody request: SignInRequest): AuthToken {
        val (email, password) = request
        return userService.logIn(email, password)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@UserContext userId: Long): String {
        return userService.getMe(userId)
    }
    
}