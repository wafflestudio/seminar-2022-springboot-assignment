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
    fun createUser(@RequestBody createUserRequest:CreateUserRequest){
        userService.createUser(createUserRequest.nickname,createUserRequest.email,createUserRequest.password)
    }
    
    @PostMapping("/login")
    fun login(@RequestBody loginUserRequest: LoginUserRequest):Long{
        return userService.login(loginUserRequest.email,loginUserRequest.password)
    }
    
    @GetMapping("/user/me")
    fun getMyInfo(@RequestHeader("X-User-ID") id:Long):UserEntity{
        return userService.getMyInfo(id)
    }
    
    @PostMapping("/survey")
    fun survey(@RequestHeader("X-User-ID") id:Long, @RequestBody surveyRequest: SurveyRequest){
        userService.survey(surveyRequest, id)
    }
}