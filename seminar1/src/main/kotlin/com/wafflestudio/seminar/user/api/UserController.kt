package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Email

@RestController
@RequestMapping("/api/v1")
class UserController(){
    @PostMapping("/user")
    fun signup(@RequestBody name:String,email: String){
        val newUser=UserEntity(name,email);
        
    }
}