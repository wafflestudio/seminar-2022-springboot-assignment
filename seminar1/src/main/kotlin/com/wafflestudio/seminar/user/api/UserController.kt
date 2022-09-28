package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.request.ReadUserRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/")
class UserController(
    private val service: UserService
) {
    @PostMapping("/user")
    fun newUser(
        @RequestBody createUserRequest: CreateUserRequest
    ):ResponseEntity<Any> {
        val newUser = service.createUser(createUserRequest)
        return ResponseEntity.ok().body(true)
    }
    
    @PostMapping("/login")
    fun findUser(
        @RequestBody loginUserRequest: LoginUserRequest
    ):ResponseEntity<Any> {
        val userId = service.loginUser(loginUserRequest)
        return ResponseEntity.ok().body(userId)
    }
    
    @GetMapping("/user/me")
    fun readUser(
        @RequestHeader("X-User-ID") userId: Long 
    ):ResponseEntity<Any> {
        val userInfo = service.readUser(userId)
        return ResponseEntity.ok().body(userInfo)
    }
}