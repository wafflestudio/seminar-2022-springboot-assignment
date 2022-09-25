package com.wafflestudio.seminar.user.api.response

import com.wafflestudio.seminar.user.domain.UserEntity

data class CreateUserResponse(
    val nickname: String,
    val email: String,
) {
    companion object {
        fun from(user: UserEntity): CreateUserResponse {
            return CreateUserResponse(
                nickname = user.nickname,
                email = user.email,
            )
        }
    }
}