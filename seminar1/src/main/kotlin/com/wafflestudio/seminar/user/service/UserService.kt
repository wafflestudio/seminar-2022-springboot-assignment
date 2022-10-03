package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserEntity
import java.util.*

interface UserService {
    fun getAll(): List<UserEntity>
    fun getUserById(id: Long): Optional<UserEntity>
    fun createUser(user: UserEntity): UserEntity
}
