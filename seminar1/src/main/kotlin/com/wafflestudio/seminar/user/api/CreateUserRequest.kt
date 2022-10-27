package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.database.UserEntity

data class CreateUserRequest(
    val name: String, 
    val email: String,
    val password: String,
) {
    
    fun wrapEntity(password: String): UserEntity {
        return UserEntity(name, email, password)
    }
}