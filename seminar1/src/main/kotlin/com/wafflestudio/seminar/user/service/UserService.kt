package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun getUser(id: Long?): User
    fun createUser(user: CreateUserRequest): User
    fun loginUser(user: LoginUserRequest): Long
}
