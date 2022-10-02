package com.wafflestudio.seminar.user

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInRequest
import com.wafflestudio.seminar.user.domain.SignInResponse
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
) {
    
    @PostMapping("/user")
    fun signUp(
        @RequestBody request: CreateUserRequest,
    ) = userService.signUp(request)

    @PostMapping("/login")
    fun signIn(
        @RequestBody request: SignInRequest,
    ) = userService.signIn(request)

    @GetMapping("/user/me")
    fun getUserMe(
        @RequestHeader("X-User-Id") id: Long?
    ) = userService.getUserMe(id)
}