package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.core.user.api.request.UpdateUserRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/api/v1/user/{user_id}")
    fun getUser(@RequestAttribute userId: Long): User {
        return userService.getUser(userId).toUser()
    }
    
    @PutMapping("/api/v1/user/me")
    fun updateUser(
        @RequestAttribute userId: Long,
        @RequestBody request: UpdateUserRequest
    ): User {
        return userService.updateUser(userId, request).toUser()
    }
}