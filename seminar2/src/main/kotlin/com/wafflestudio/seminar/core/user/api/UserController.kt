package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.EditUserRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun getUserInfo(@PathVariable(value = "user_id") userId: Long): UserResponse {
        return userService.findUserById(userId)
    }
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun editUserInfo(@UserContext loginUser: UserEntity, @RequestBody request: EditUserRequest): UserResponse {
        return userService.editUserInfo(loginUser, request)
    }
    
    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun participate(@UserContext loginUser: UserEntity, @RequestBody request: ParticipateRequest): UserResponse {
        return userService.participate(loginUser, request) 
    }
    
}