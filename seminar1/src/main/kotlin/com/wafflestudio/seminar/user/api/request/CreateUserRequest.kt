package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.database.UserEntity

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
) {
    fun toEntity(): UserEntity {
        return UserEntity(null, name, email, password)
    }
}