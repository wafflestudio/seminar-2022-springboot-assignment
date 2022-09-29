package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository
        ){
    fun signUp(newUser: UserEntity):UserEntity{
        userRepository.signUpUser(newUser);
        return newUser
    }
}