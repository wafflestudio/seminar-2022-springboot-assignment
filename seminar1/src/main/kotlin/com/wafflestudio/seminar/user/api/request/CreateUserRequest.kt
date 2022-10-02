package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
) {
    fun toDomain(): UserEntity {
        return UserEntity(emptyList(), name, email, password)
    }
}