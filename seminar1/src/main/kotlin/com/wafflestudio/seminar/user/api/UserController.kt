package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/user")
    fun createUser(
        @Valid @RequestBody createUserRequest : CreateUserRequest,
    ): ResponseEntity<Any> {
        val user = userService.createUser(createUserRequest)
        return ResponseEntity("${user.nickname}님의 회원가입이 완료되었습니다.", HttpStatus.CREATED)
    }
    
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest : LoginRequest,
    ): ResponseEntity<Any> {
        val userId = userService.login(loginRequest)
        return ResponseEntity("로그인이 완료되었습니다. 유저 ID : $userId", HttpStatus.OK)
    }
    
    @GetMapping("/user/me")
    fun getUserInfo(
        @RequestHeader("X-User-ID") xUserId : Long,
    ): ResponseEntity<Any> {
        val userInfo = userService.getUserInfo(xUserId)
        return ResponseEntity(
            "아이디 : ${userInfo.userId}\n" +
                    "닉네임 : ${userInfo.nickname}\n" +
                    "이메일 : ${userInfo.email}\n",
            HttpStatus.OK
        )
    }
    
}