package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest

interface UserPort {
    fun createUser(signUpRequest: SignUpRequest): User

    fun getUser(loginRequest: LoginRequest): User
}