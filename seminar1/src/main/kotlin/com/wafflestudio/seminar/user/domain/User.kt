package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.user.database.UserEntity

data class User(
    val nickname: String,
    val email: String
    
) {
    companion object { 
        fun from(userEntity: UserEntity): User {
            return User(
                userEntity.name, userEntity.email
            )
        }
    }
}