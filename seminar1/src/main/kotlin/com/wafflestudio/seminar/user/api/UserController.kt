package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
        private val userService: UserService
) {
    @PostMapping("/api/v1/user")
    fun createUser(@RequestBody userRequest: CreateUserRequest) {
        // TODO:
        // When Field is not included, it throws 400 bad request...
        // Can we customize it...??
        userService.createUser(userRequest)
    }

}