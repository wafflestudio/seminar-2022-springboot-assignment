package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.exception.Seminar403
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.lecture.service.UserService
import com.wafflestudio.seminar.user.api.request.*
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {
    
    @PostMapping("/api/v1/user")
    fun create(
        @RequestBody req: CreateUserRequest
    ) {
        userService.register(req.nickname, req.email, req.password)
    }
    
    @PostMapping("/api/v1/login")
    fun login(
        @RequestBody req: LoginRequest
    ) : Long{
        return userService.login(req.email, req.password)!!
    }
    
    @GetMapping("/api/v1/user/me")
    fun me(
        @RequestHeader("X-User-ID") id : Long?
    ) : UserEntity {
        if(id != null)
        {
            return userService.findById(id)
        }else{
            throw(Seminar403("id를 입력해주세요."))
        }
    }

}