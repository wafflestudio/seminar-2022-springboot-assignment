package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity

interface UserPort {
    fun createUser(signUpRequest: SignUpRequest): UserEntity

    fun getUser(signInRequest: SignInRequest): UserEntity


    fun getUserIdByEmail(email: String): Long

    fun getProfile(userId: Long): ProfileResponse

//    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest): UserProfileResponse
//
//    fun registerParticipant(userId: Long, registerParticipantRequest: RegisterParticipantRequest)
}