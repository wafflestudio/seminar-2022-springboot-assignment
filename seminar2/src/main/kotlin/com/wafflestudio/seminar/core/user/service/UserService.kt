package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User

interface UserService {
    fun register(signUpRequest: SignUpRequest): Long
    fun findOne(userId: Long): User
    fun update(): Long
}