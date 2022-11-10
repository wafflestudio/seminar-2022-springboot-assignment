package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.User

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): Long
    fun login(email: String, password: String)
    fun getProfile(userId: Long): User
    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest)
    fun registerParticipantProfile(userId: Long, participantRequest: ParticipantRequest)
}