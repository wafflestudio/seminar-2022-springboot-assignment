package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1")
class UserController(private val service: UserService) {
    
    @GetMapping("")
    fun index(){
        
    }

    @GetMapping("/find")
    fun find(): List<UserEntity>? {
        return service.findAll()
    }
    
    @PostMapping("/user")
    fun save( @RequestBody user: UserEntity
    ):User {
        
        return service.save(user)
    }
    
    @PostMapping("/login")
    fun login( @RequestBody user: UserLogin, @RequestHeader("X-User-Id") value: String ): User {
        println(value)
        return service.findByEmailAndPassword(user)
        
    }
    
     
}


