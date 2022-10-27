package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.CreateUserRequest
import com.wafflestudio.seminar.user.service.UserService
import com.wafflestudio.seminar.user.service.UserServiceImpl
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController (
    private val userService: UserService
) {
    @PostMapping("/user")
    fun CreateUser(@RequestBody request: CreateUserRequest) = userService.CreateUser(request)
    
    @PostMapping("/login")
    fun Login(@RequestBody request: LoginRequest) = userService.Login(request)
    
    @GetMapping("/user/me")
    fun getUser(@RequestHeader("X-User-Id") id: Long?) = userService.GetUser(id)
    
    @PostMapping("/survey")
    fun getUserSurvey(@RequestHeader("X-User-Id") id: Long?, @RequestBody request: CreateSurveyRequest) = userService.GetUserSurvey(id, request)
}