package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInUserRequest
import com.wafflestudio.seminar.user.api.response.LoginInfo
import com.wafflestudio.seminar.user.api.response.userInfo
import com.wafflestudio.seminar.user.exception.ErrorCode
import com.wafflestudio.seminar.user.exception.UserException
import com.wafflestudio.seminar.user.exception.UserUnauthorized
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@ResponseBody
@RequestMapping("/api/v1")
class UserController (
    val userService : UserService
        ) {
    
    @PostMapping("/user")
    fun signUp(@RequestBody createUserRequest: CreateUserRequest) : userInfo {
        return userService.signUp(createUserRequest)
    }
    
    @PostMapping("/login")
    fun signIn(@RequestBody signInUserRequest: SignInUserRequest): LoginInfo{
        return userService.signIn(signInUserRequest)
    }
    
    @GetMapping("/user/me")
    fun getUserInfo(@RequestHeader("X-USER-ID") userId: Long?): userInfo{
        userId ?: throw UserUnauthorized()
        return userService.getUserInfo(userId)
    }
    
    
}