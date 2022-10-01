package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/api/v1/user")
    fun createUser(@RequestBody request: CreateUserRequest) = userService.createUser(request)

    @PostMapping("/api/v1/login")
    fun login(@RequestBody request: LoginRequest) = userService.login(request)
}