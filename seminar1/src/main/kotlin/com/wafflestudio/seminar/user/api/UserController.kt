package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/api/v1/user")
    fun createUser(@Valid @RequestBody request: CreateUserRequest) = userService.createUser(request)

    @PostMapping("/api/v1/login")
    fun login(@Valid @RequestBody request: LoginRequest) = userService.login(request)
    
    @GetMapping("/api/v1/user/me", headers = ["X-User-ID"])
    fun getUser(@RequestHeader("X-User-ID") xUserId: Long) = userService.getUser(xUserId)
}