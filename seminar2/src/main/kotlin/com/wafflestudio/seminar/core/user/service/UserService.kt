package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): Long
    fun findOne(userId: Long): User
    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest)
    fun beParticipant(userId: Long, participantRequest: ParticipantRequest)
}