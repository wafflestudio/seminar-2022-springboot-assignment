package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun getAll(): List<UserEntity>
    fun getUserById(id: Long): User
    fun createUser(user: CreateUserRequest): User
}
