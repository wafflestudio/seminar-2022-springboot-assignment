package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun createUser(request: CreateUserRequest): User
    fun login(request: LoginRequest): Long
    fun getUser(xUserId: Long): User
}