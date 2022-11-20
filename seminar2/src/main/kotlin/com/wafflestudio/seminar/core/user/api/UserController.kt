package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.EditUserRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import com.wafflestudio.seminar.core.user.service.UserServiceImpl
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.*

@RestController
class UserController (
    private val userService: UserServiceImpl,
    private val authTokenService: AuthTokenService
    ) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody req: SignUpRequest) : AuthToken {
        return userService.signUp(req)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestParam email: String, @RequestParam pwd: String) : AuthToken {
        return userService.logIn(email, pwd)
    }
    
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun getUser(@PathVariable user_id: Long) : UserResponse {
        return userService.getUser(user_id)
    }

    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun editUser(@RequestHeader(value="Authorization") token: String, @RequestBody req: EditUserRequest) : UserResponse {
        return userService.editUser(req, authTokenService.getCurrentUserId(token))
    }
    
    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun makeParticipant(@RequestHeader(value="Authorization") token: String, @RequestBody req: ParticipantRequest) : UserResponse {
        return userService.makeParticipant(req, authTokenService.getCurrentUserId(token))
    }



}