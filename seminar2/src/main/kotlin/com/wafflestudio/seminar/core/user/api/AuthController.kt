package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authTokenService: AuthTokenService,
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
        @RequestBody signInRequest: SignInRequest
    ): AuthToken {
        return userService.signIn(signInRequest)
    }

//    @Authenticated
//    @GetMapping("/me")
//    fun getMe(
//        @RequestHeader("Authorization") authHeader: String,
//        @UserContext userId: Long
//    ): ProfileResponse {
//        return userService.getProfile(userId)
//    }

//    @Authenticated
//    @GetMapping("/user/{user_id}")
//    fun getProfile(
//        @RequestHeader("Authorization") authHeader: String,
//        @PathVariable user_id: Long
//    ): User {
//        return authTokenService.getProfile(user_id)
//    }
//
//    @Authenticated
//    @PutMapping("/user/me")
//    fun editProfile(
//        @RequestHeader("Authorization") authHeader: String,
//        @RequestBody editProfileRequest: EditProfileRequest,
//        @UserContext userId: Long
//    ): User {
//        return authTokenService.editProfile(userId, editProfileRequest)
//    }
//
//    @Authenticated
//    @PostMapping("/user/participant")
//    fun registerParticipant(
//        @RequestHeader("Authorization") authHeader: String,
//        @RequestBody registerParticipantRequest: RegisterParticipantRequest,
//        @UserContext userId: Long
//    ): User {
//        return authTokenService.registerParticipant(userId, registerParticipantRequest)
//    }
}