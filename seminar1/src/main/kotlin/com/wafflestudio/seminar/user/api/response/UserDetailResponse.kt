package com.wafflestudio.seminar.user.api.response

import com.wafflestudio.seminar.user.database.UserEntity

data class UserDetailResponse(
    val nickname: String,
    val email: String,
) {
    companion object {
        fun from(user: UserEntity): UserDetailResponse {
            return UserDetailResponse(
                nickname = user.nickname,
                email = user.email,
            )
        }
    }
}
