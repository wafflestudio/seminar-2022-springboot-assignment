package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.exception.ErrorCode
import com.wafflestudio.seminar.exception.SeminarException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/user")
    fun postUser(
        @RequestBody req: CreateUserRequest
    ): String {
        // 유저의 이름과 이메일이 공란이거나 빈칸(공백)으로만 이루어질 수 없음
        if (req.nickname.run { this.isEmpty() || this.isBlank() }
            || req.email.run { this.isEmpty() || this.isBlank() }
        ) throw SeminarException(ErrorCode.INVALID_PARAMETER)
        else return userService.register(req.nickname, req.email, req.password)
    }
    
    @PostMapping("/login")
    fun login(
        @RequestHeader(value="email") email: String,
        @RequestHeader(value="password") pwd: String,
    ): Long = userService.login(email, pwd)

    @GetMapping("/user/me")
    fun getUser(
        @RequestHeader(value="X-User-ID", required=false) userId: Long?,
    ): User {
        if (userId == null) throw SeminarException(ErrorCode.FORBIDDEN)
        else return userService.getUser(userId)
    }
}