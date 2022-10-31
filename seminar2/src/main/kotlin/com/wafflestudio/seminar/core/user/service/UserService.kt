package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.ProfileResponse
import com.wafflestudio.seminar.core.user.domain.UserPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userPort: UserPort,
    private val authTokenService: AuthTokenService,
) {
    fun signUp(signUpRequest: SignUpRequest): AuthToken {
        val user = userPort.createUser(signUpRequest)
        return authTokenService.generateTokenByEmail(user.email)
    }

    fun signIn(signInRequest: SignInRequest): AuthToken {
        val user = userPort.getUser(signInRequest)
        return authTokenService.generateTokenByEmail(user.email)
    }

    fun getProfile(userId: Long): ProfileResponse {
        return userPort.getProfile(userId)
    }
//
//    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest): User {
//        return userPort.editProfile(userId, editProfileRequest)
//    }
//
//    fun registerParticipant(userId: Long, registerParticipantRequest: RegisterParticipantRequest): User {
//        return userPort.registerParticipant(userId, registerParticipantRequest)
//    }
}