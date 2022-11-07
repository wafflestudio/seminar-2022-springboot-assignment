package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.PutUserRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.GetUserResponse
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.*

@RestController
class AuthController (
        private val authService: AuthService,
        private val authTokenService: AuthTokenService
        ) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody request : SignUpRequest) = authService.signUp(request)
    
    @PostMapping("/api/v1/signin")
    fun LogIn(@RequestBody request : LogInRequest) = authService.logIn(request)
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@RequestHeader token: String): GetUserResponse {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return authService.getUser(id)
    }

    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun getUser(@PathVariable user_id: Long) = authService.getUser(user_id)
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun putUser(@RequestHeader token: String, @RequestBody request : PutUserRequest) : GetUserResponse{
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return authService.putUser(id, request)   
    }
    
    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun postParticipant(@RequestHeader token: String, @RequestBody request: ParticipantRequest) : GetUserResponse {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return authService.postParticipant(id, request)
    }
}