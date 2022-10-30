package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.domain.UserResponse
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
class UserController(
        private val userService: UserService,
) {
    @PostMapping("/user")
    fun createUser(
            @RequestBody req: CreateUserRequest
    ) = userService.createUser(req)
    
    @PostMapping("/login")
    fun login(
            @RequestBody req: LoginUserRequest
    ) = userService.login(req)
    
    @GetMapping("/user/me")
    fun getUser(
            @RequestHeader(value = "X-User-ID", required = false) id: Long?
    ) : UserResponse{
        if (id == null) throw MissingHeaderException("유저를 식별할 수 없습니다.")
        return userService.getUser(id)
    }
}