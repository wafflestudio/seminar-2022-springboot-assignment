package com.wafflestudio.seminar.user.api.response

import com.wafflestudio.seminar.user.domain.User

data class UserResponse(
    val nickname: String,
    val email: String
    
) {
    companion object { 
        fun from(user: User): UserResponse {
            return UserResponse(
                user.name, user.email
            )
        }
    }
}