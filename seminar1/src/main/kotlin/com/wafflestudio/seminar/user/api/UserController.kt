package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.exception.Seminar403
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.domain.User

import com.wafflestudio.seminar.user.service.UserService

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/user/me")
    fun getUser(
        @RequestHeader(value = "X-User-ID", required = false) id: Int?
    ): User {
        if (id == null) {
            throw Seminar403("헤더를 입력하지 않았습니다.")
        } else {
            return userService.getUser(id)
        }
    }

    @PostMapping("/user")
    fun createUser(
        @RequestBody req: CreateUserRequest
    ) : String {
        userService.register(req.nickname, req.email, req.password)
        return "회원가입이 완료되었습니다."
    }

    @PostMapping("/login")
    fun login(
        @RequestHeader(value = "email", defaultValue = "email") email: String,
        @RequestHeader(value = "password", defaultValue = "password") password: String
    ): Long {
        return userService.login(email, password)
    }
    
    

}