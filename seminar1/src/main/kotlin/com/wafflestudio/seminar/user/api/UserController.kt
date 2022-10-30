package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInUserRequest
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/user")
    fun createUser(
        @RequestBody
        createUserRequest: CreateUserRequest        
    ): String {
        return userService.createUser(createUserRequest)
    }
    
    @PostMapping("/login")
    fun signIn(
        @RequestBody
        signInUserRequest: SignInUserRequest
    ): Long {
        return userService.signInUser(signInUserRequest)
    }
    
    @GetMapping("/user/me")
    fun getUserInfo(
        @RequestHeader(value = "X-User-ID")
        id: Long
    ): User {
        return userService.getUserInfo(id)
    }
}