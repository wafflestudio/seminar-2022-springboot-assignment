package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.domain.User

interface UserService {
    fun createUser(signUpRequest: SignUpRequest): AuthToken
    fun loginUser(loginRequest: LoginRequest):AuthToken
    fun getUser(authToken: String): String
    fun getProfile(userId: Long): User
    fun modifyProfile(authToken: String, modifyRequest: ModifyRequest): User
    fun addNewParticipant(authToken: String, registerParticipantRequest: RegisterParticipantRequest): User
}