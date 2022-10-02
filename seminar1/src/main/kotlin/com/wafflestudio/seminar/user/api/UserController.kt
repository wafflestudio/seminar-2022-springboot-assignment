package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {
    
    @PostMapping("/api/v1/user")
    fun createUser(@RequestBody requestBody:CreateUserRequest){
        userService.createUser(requestBody.nickname,requestBody.email,requestBody.password)
    }
    
    @PostMapping("/api/v1/login")
    fun login(@RequestBody requestBody: LoginUserRequest):Long{
        return userService.login(requestBody.email,requestBody.password)
    }
    
    @GetMapping("/api/v1/user/me")
    fun getMyInfo(@RequestHeader("X-User-ID") id:Long):UserEntity{
        return userService.getMyInfo(id)
    }
    
}