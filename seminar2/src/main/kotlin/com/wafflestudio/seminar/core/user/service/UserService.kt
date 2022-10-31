package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.User

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): Long
    fun login(email: String, password: String)
    fun getProfile(userId: Long): User
    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest)
    fun registerParticipantProfile(userId: Long, participantRequest: ParticipantRequest)
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): Seminar
    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): Seminar
    fun getSeminar(seminarId: Long): Seminar
    fun getSeminars(name: String, order: String): List<SeminarResponse>
    fun joinSeminar(userId: Long, seminarId: Long, role: Role): Seminar
    fun dropSeminar(userId: Long, seminarId: Long): Seminar
}