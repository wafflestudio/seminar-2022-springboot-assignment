package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.ProfileResponse
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid signUpRequest: SignUpRequest
    ): AuthToken {
        return userService.signUp(signUpRequest)
    }

    @PostMapping("/signin")
    fun signIn(
        @RequestBody @Valid signInRequest: SignInRequest
    ): AuthToken {
        return userService.signIn(signInRequest)
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(
        @RequestHeader("Authorization") authHeader: String,
        @UserContext userId: Long
    ): ProfileResponse {
        return userService.getProfile(userId)
    }

    @Authenticated
    @GetMapping("/user/{user_id}")
    fun getProfile(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable user_id: Long
    ): ProfileResponse {
        return userService.getProfile(user_id)
    }

    @Authenticated
    @PutMapping("/user/me")
    fun editProfile(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody @Valid editProfileRequest: EditProfileRequest,
        @UserContext userId: Long
    ): ProfileResponse {
        return userService.editProfile(userId, editProfileRequest)
    }

    @Authenticated
    @PostMapping("/user/participant")
    fun registerParticipant(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody @Valid registerParticipantRequest: RegisterParticipantRequest,
        @UserContext userId: Long
    ): ProfileResponse {
        return userService.registerParticipant(userId, registerParticipantRequest)
    }
}