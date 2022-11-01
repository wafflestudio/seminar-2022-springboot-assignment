package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/api/v1/user/{user_id}")
    fun getUser(@PathVariable("user_id") userId: Long): User {
        return userService.getUser(userId)
    }
}