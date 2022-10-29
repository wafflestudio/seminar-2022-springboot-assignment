package com.wafflestudio.seminar.user.api.response

import com.wafflestudio.seminar.user.database.UserEntity

data class userInfo(
    val userName : String,
    val email: String,
) {
    companion object{
        fun toDTO(user: UserEntity): userInfo{
            return userInfo(userName = user.userName, email = user.email)
        }
    }
}

data class LoginInfo(
    val userId: Long
){
    companion object{
        fun toDTO(user: UserEntity): LoginInfo{
            return LoginInfo(userId = user.id)
        }
    }
}
