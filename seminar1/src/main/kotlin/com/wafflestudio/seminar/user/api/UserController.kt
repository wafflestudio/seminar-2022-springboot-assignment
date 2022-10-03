package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
        private val userService: UserService,
) {
    
    @PostMapping("api/v1/user")
    fun postUser(
            // @RequestBody user: CreateUserRequest
            @RequestParam nickname: String,
            @RequestParam email: String,
            @RequestParam password: String,
    ): String{
        return userService.register(CreateUserRequest(nickname, email, password))
    }
    
    @PostMapping("api/v1/login")
    fun login(
            // @RequestBody user: LoginUserRequest
            @RequestParam email: String,
            @RequestParam password: String,
    ): String{
        return userService.login(LoginUserRequest(email, password))
    }
    
    @GetMapping("api/v1/user/me")
    fun getUser(
            @RequestHeader("X-User-ID") id: Long?
    ): String{
        id ?: throw Seminar403("id를 입력해주세요.")
        return userService.getUser(id)
    }
    
}