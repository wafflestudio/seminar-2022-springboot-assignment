package com.wafflestudio.seminar.user.api.response
import com.wafflestudio.seminar.user.database.UserEntity

data class UserResponse (
    val id: Long,
    val nickname: String,
    val email: String,
    val password: String,
){
    companion object{
        fun of(user: UserEntity): UserResponse{
            return UserResponse(
                id=user.id!!,
                nickname=user.nickname,
                email=user.email,
                password = user.password
            )
        }
    }
}