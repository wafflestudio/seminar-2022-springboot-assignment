package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val userService: UserService,
    private val authTokenService: AuthTokenService
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): AuthToken {
        userService.signUp(signUpRequest)
        return authTokenService.generateTokenByEmail(signUpRequest.email!!)
    }

    @PostMapping("/signin")
    fun logIn(@RequestBody @Valid loginRequest: LoginRequest): AuthToken {
        userService.login(loginRequest.email!!, loginRequest.password!!)
        return authTokenService.generateTokenByEmail(loginRequest.email)
    }

}