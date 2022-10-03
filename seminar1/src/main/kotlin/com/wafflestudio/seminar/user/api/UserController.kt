package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.request.SurveyRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserController(private val userService: UserService) {
    
    @PostMapping("/user")
    fun createUser(@RequestBody createUserRequest:CreateUserRequest):String{
        userService.createUser(createUserRequest.nickname,createUserRequest.email,createUserRequest.password)
        return "User Created"
    }
    
    @PostMapping("/login")
    fun login(@RequestBody loginUserRequest: LoginUserRequest):String{
        val userId = userService.login(loginUserRequest.email, loginUserRequest.password)
        return "Login Success, User Id: ${userId}"
    }
    
    @GetMapping("/user/me")
    fun getMyInfo(@RequestHeader("X-User-ID") id:Long):UserEntity{
        return userService.getMyInfo(id)
    }
    
    @PostMapping("/survey")
    fun survey(@RequestHeader("X-User-ID") id:Long, @RequestBody surveyRequest: SurveyRequest):String{
        userService.survey(surveyRequest, id)
        return "Survey Created"
    }
}