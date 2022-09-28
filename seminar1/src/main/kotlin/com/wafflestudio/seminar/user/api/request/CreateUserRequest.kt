package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.database.UserEntity

data class CreateUserRequest(
    val nickname: String,
    val email: String,
    var password: String,
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            userName=nickname, 
            email=email, 
            password=password
        )
    }
}