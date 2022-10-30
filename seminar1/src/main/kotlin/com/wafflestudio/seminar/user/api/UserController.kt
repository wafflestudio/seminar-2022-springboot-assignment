package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.service.UserService
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController(
        private val userService: UserService
) {
    @PostMapping("/user")
        fun postUser(@RequestBody user: CreateUserRequest): String{
            return userService.register(user)
    }
    
    @PostMapping("/login")
    fun login(@RequestBody user: LoginUserRequest): String{
        return userService.login(user)
    }

    @GetMapping("/user/me")
    fun getUser(
            @RequestHeader("X-User-ID") id: Long?
    ): String{
        id ?: throw Seminar403("id를 입력해주세요.")
        return userService.getUser(id)
    }
}