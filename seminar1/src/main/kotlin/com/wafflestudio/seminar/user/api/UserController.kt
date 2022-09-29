package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
){
    @PostMapping("/user")
    fun signup(@RequestBody name:String,email: String):UserEntity{
        val newUser=UserEntity(name,email);
        userService.signUp(newUser);
        return newUser
    }
    
    @PostMapping("/login")
    fun login(@RequestBody email: String,password:String):Long{
        return 1;
    }

    @GetMapping("/user/me")
    fun getInfo(@RequestHeader("X-User-ID") id: Long): UserEntity? {
        return userService.getUserById(id)
    }
    @PostMapping("/survey")
    fun addSurvey(@RequestBody spring_exp:String,rdb_exp:String,programming_exp:String,os:String){
        
    }
}