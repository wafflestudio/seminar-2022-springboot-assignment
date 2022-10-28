package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest

interface UserPort {
    fun createUser(signUpRequest: SignUpRequest): User

    fun getUser(signInRequest: SignInRequest): User
}