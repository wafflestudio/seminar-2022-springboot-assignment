package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.user.database.UserEntity

data class User(
    val userName: String,
    val email: String,
    val password: String
){
    companion object {
        fun to(userEntity: UserEntity): User {
            return User(userEntity.userName,userEntity.email,"비공개")
        }
    }
}