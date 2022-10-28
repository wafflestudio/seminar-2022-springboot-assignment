package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest

interface UserPort {
    fun getUser(id: Long): User
    fun createUser(signUpRequest: SignUpRequest): User
}