package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun createUser(createUserRequest: CreateUserRequest): User
    fun login(loginRequest: LoginRequest): Long
    fun getUserInfo(xUserId: Long): User
}