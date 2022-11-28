package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.UserDTO
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
class AuthController (
    private val userService: UserService
){
    @Transactional
    @PostMapping("/api/v1/signup")
    fun signUp(
        @Valid @RequestBody request: SignUpRequest
    ): AuthToken = userService.signUp(request)
    
    @Transactional
    @PostMapping("/api/v1/signin")
    fun logIn(
        @Valid @RequestBody request: SignInRequest
    ): AuthToken = userService.logIn(request.email!!, request.password!!)
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long 
    ): UserDTO = userService.getMe(userId)
    
}