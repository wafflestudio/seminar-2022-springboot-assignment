package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.request.ReadUserRequest
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun createUser(user: CreateUserRequest): User
    fun loginUser(user: LoginUserRequest): Long
    fun readUser(userId: Long): User
}