package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.exception.UserException403
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController (
    private val service: UserService,
) {
    
    @PostMapping("/user")
    fun createUser(
        @RequestBody request: CreateUserRequest
    ): String {
        service.createUser(request.nickname, request.email, request.password)
        return "creation successful"
    }
    
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): String {
        service.login(request.email, request.password)
        return "login successful"
    }
    
    @GetMapping("/me")
    fun myInfo(
        @RequestHeader("X-User-Id") id: Long?
    ) = service.myInfo(id ?: throw UserException403("cannot identify user"))
}