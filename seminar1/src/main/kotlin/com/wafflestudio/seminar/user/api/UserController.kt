package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.service.UserService
import com.wafflestudio.seminar.user.domain.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/user/me")
    fun getUser(
        @RequestHeader("X-User-ID") id: Long?
    ): User {
        return userService.getUser(id)
    }
    
    @PostMapping("/login")
    fun login(
        @RequestBody user: LoginUserRequest
    ) = userService.loginUser(user)
    
    @PostMapping("/user")
    fun create(
        @RequestBody user: CreateUserRequest
    ) = userService.createUser(user)
}