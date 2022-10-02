package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.api.response.CreateSurveyResponse
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
class UserController(
    private val userService: UserServiceImpl,
) {
    @PostMapping("/user/signup")
    fun signUp(@RequestBody @Valid  request: CreateUserRequest): UserResponse{
        return userService.saveUser(request)!!
    }

    @PostMapping("/user/login")
    fun login(@RequestBody request: LoginUserRequest): Long{
        val UserResponse =userService.login(request)!!
        return UserResponse.id
    }

    @GetMapping("/user/me")
    fun userInfo(@RequestHeader("X-User-ID") id: Long?): UserDetailResponse? {
        if(id==null){throw Forbidden()}
        val user = userService.findById(id) ?: throw UserNotFound()
        user?.let {
            user.survey?.let { it1 -> return UserDetailResponse(user.nickname, user.email, user.password, it1.toSurveyResponse()) }?:run{
                return UserDetailResponse(user.nickname, user.email, user.password,null)
            }
        }
    }
    
    @PostMapping("/user/survey")
    fun participate(@RequestBody @Valid  request: CreateSurveyRequest, @RequestHeader("X-User-ID") id: Long,bindingResult: BindingResult):CreateSurveyResponse? {
        if (bindingResult.hasErrors()) {
            throw ResponseRequired()
        }
        
        return userService.participate(id,request)
    }

    @GetMapping("/userlist")
    fun getUserList() =
        userService.userList()
}