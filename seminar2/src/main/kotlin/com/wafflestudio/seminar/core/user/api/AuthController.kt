package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.ModifyRequest
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val service: UserService,
) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest
    ): AuthToken = service.createUser(signUpRequest)
    
    @PostMapping("/api/v1/signin")
    fun logIn(
        @RequestBody loginRequest: LoginRequest
    ): AuthToken = service.loginUser(loginRequest)
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @RequestHeader("Authorization") authToken: String
    ) = service.getUser(authToken)
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun profileDetails(
        @RequestHeader("Authorization") authToken: String,
        @PathVariable(value = "user_id") userId: Long
    ) = service.getProfile(userId)
    
    @Authenticated
    @PutMapping("/api/v1/user/me/")
    fun profileModify(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody modifyRequest: ModifyRequest
    ) = service.modifyProfile(authToken, modifyRequest)
    
    @Authenticated
    @PostMapping("/api/v1/user/participant/")
    fun registerParticipant(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody registerParticipantRequest: RegisterParticipantRequest
    ) = service.addNewParticipant(authToken, registerParticipantRequest)
}