package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.CreateUserResponse
import com.wafflestudio.seminar.user.api.response.LoginUserResponse
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.exception.UserUnauthorizedException
import com.wafflestudio.seminar.user.service.UserService
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserController(
    val userService: UserService,
) {
    
    @PostMapping("/user")
    fun signup(@RequestBody @Valid createUserRequest: CreateUserRequest): CreateUserResponse {
        return userService.signup(createUserRequest)
    }
    
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginUserRequest: LoginUserRequest): LoginUserResponse {
        return userService.login(loginUserRequest)
    }
    
    @GetMapping("/user/me")
    fun userInfo(@RequestHeader("X-User-ID") userId: Long?): UserDetailResponse {
        return userService.findById(userId ?: throw UserUnauthorizedException())
    }
    
}