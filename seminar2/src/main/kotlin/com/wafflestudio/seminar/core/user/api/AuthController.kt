package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.JwtResponse
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val userService: UserService,
) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): JwtResponse {
        return userService.signUp(request)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@Valid @RequestBody request : LoginRequest): JwtResponse {
        return userService.login(request)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@UserContext loginUser: UserEntity): UserResponse {
        return UserResponse.of(loginUser)
    }
}