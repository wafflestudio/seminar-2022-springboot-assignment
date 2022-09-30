package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.service.UserServiceImpl
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
    private val userService: UserServiceImpl,
) {
    @PostMapping("/user/signup")
    fun signUp(@RequestBody request: CreateUserRequest){
        userService.saveUser(request)
    }

    @PostMapping("/user/login")
    fun login(@RequestBody request: LoginUserRequest) {
        userService.login(request)
    }

    @GetMapping("/user/me/{userId}")
    fun userInfo(@PathVariable userId: Long, @RequestHeader("X-User-ID") id: Long): UserDetailResponse? {
        if(id==null){throw Forbidden()}
        val user = userService.findById(id)
        user?.let {
            user.survey?.let { it1 -> return UserDetailResponse(user.nickname, user.email, user.password, it1) }
        } ?: run {
            throw UserNotFound()
        }
    }
    
    @PostMapping("/user/survey")
    fun participate(@RequestBody request: CreateSurveyRequest, @RequestHeader("X-User-ID") id: Long) {
        userService.participate(id,request)
    }

    @GetMapping("/userlist")
    fun getUserList() =
        userService.userList()
}