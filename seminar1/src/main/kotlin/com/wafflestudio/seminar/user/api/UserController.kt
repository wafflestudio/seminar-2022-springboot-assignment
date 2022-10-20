package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController (private val userService: UserService) {
    
    @PostMapping("/user")
    fun makeUser(@RequestBody user: CreateUserRequest) = userService.makeUser(user)
    
    @PostMapping("/login")
    fun loginUser(@RequestHeader("email") email: String,
                  @RequestHeader("password") pwd: String) = userService.loginUser(email, pwd)
    
    @GetMapping("/user/me")
    fun getUser(@RequestHeader("X-User-ID") userId: Long?) = userService.getUser(userId)
    
}