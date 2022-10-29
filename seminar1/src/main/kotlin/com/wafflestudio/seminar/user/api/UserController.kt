package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/")
class UserController(
    private val service: UserServiceImpl
) {
    @PostMapping("/user")
    fun newUser(
        @RequestBody createUserRequest: CreateUserRequest
    ):ResponseEntity<Any> {
        val newUser = service.createUser(createUserRequest)
        return ResponseEntity.ok().body(newUser.userName + "님, 환영합니다")
    }
    
    @PostMapping("/login")
    fun findUser(
        @RequestBody loginUserRequest: LoginUserRequest
    ):ResponseEntity<Any> {
        val userId = service.loginUser(loginUserRequest)
        return ResponseEntity.ok().body("아이디: " + userId)
    }
    
    @GetMapping("/user/me")
    fun readUser(
        @RequestHeader("X-User-ID") userId: Long 
    ):ResponseEntity<Any> {
        val userInfo = service.readUser(userId)
        return ResponseEntity.ok().body(userInfo)
    }
    
}