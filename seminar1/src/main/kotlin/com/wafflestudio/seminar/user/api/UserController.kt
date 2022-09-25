package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class UserController(
    private val userService: UserService
) {
    
    private val log = LoggerFactory.getLogger(javaClass)
    
    @GetMapping("/v1/user/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ) = userService.getUser(userId)
    
    @PostMapping("/v1/user")
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest
    ): UserResponse {
        log.info("Create User request: $request")
        return userService.createUser(request)
    }
    
    @PostMapping("/v1/login")
    fun login(
        @Valid @RequestBody request: LoginUserRequest 
    ) : Map<String, Long> {
        return userService.login(request)
    }
    
    @GetMapping("/v1/user/me")
    fun getLoginedUserMe(
        @RequestHeader(value = "X-User-ID", required = true) userId: Long
    ): UserResponse {
        return userService.getLoginedUserMe(userId)
    }
}