package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.stereotype.Service


@Service
class AuthService(
    val userRepository: UserRepository
) {
    fun signUp(user: SignUpRequest) {
        if(user.username=="") throw Seminar400("");
        if(user.email=="") throw Seminar400("");
        if(userRepository.findByEmail(user.email) != null) throw Seminar409("");
        val userEntity = UserEntity(user.username, user.email, user.password)
    }
}