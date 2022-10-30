package com.wafflestudio.seminar.user.api

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
class UserController(
    private val service: UserService
) {
    @PostMapping("/user")
    fun addUser(
        @RequestBody createUserRequest: CreateUserRequest,
    ) = service.addUser(createUserRequest)

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ) = service.login(loginRequest)

    @GetMapping("/user/me")
    fun getMe(
        @RequestHeader("X-User-ID") id: Long?,
    ) = service.findUser(id)

}
