package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/api/v1/user")
    fun signUp(@RequestBody request: CreateUserRequest){
        userService.saveUser(request)
    }

    @PostMapping("/api/v1/login")
    fun login(@RequestBody request: LoginUserRequest) {
        userService.login(request)
    }

    @GetMapping("/api/v1/user/me")
    fun userInfo(@RequestHeader("X-User-ID") id: Long): UserDetailResponse? {
        val user= userService.findById(id)!!
        return user.survey?.let { UserDetailResponse(user.nickname,user.email,user.password, it) }
    }
}