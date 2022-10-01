package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.UserInfoDTO
import com.wafflestudio.seminar.user.api.request.UserLoginDTO
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

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
    
    @PostMapping("/api/v1/login")
    fun login(@RequestBody userLogin: UserLoginDTO): Long {
        // TODO:
        // When Field is not included, it throws 400 bad request...
        // Can we customize it...??
        return userService.login(userLogin)
    }
    
    @GetMapping("/api/v1/user/me")
    fun getUserInfo(
            @RequestHeader(name="X-User-ID") id: Long?,
    ): UserInfoDTO {
        // TODO: 403 Forbidden when header is not given
        return userService.getUserInfo(id ?: throw GetUserUnauthorizedException())
    }
    
}