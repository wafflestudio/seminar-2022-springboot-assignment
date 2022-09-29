package com.wafflestudio.seminar.user.api.response
import com.wafflestudio.seminar.user.domain.User

data class UserResponse (
    val id: Long,
    val nickname: String,
    val email: String,
){
    companion object{
        fun of(user: User): UserResponse{
            return UserResponse(
                id=user.id!!,
                nickname=user.nickname,
                email=user.email
            )
        }
    }
}