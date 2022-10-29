package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun join(user: CreateUserRequest)
    fun login(email : String, password: String): Long
    fun findMe(user_id : Long):User
}

@Service
class UserServiceImpl(
    private val userRepository : UserRepository,
    private val passwordEncoder : PasswordEncoder
) : UserService {

    override fun join(user: CreateUserRequest) {
        val temp = userRepository.findByEmail(user.email)
        if (temp == null) {
            userRepository.save(UserEntity(user.nickname, user.email, passwordEncoder.encode(user.password)))
        } else {
            throw Seminar409("이미 존재하는 email 입니다.")
        }

    }


    override fun login(email: String, password: String): Long {
        val userEntity = userRepository.findByEmail(email)
        if (userEntity == null) {
            throw Seminar404("존재하지 않는 email 입니다.")
        } else {
            if (passwordEncoder.matches(password, userEntity.encodedPassword)) {
                return userEntity.id
            } else {
                throw Seminar401("비밀번호가 틀립니다.")
            }
        }
    }

    override fun findMe(user_id: Long): User {
        return userRepository.findByIdOrNull(user_id)?.toUser() ?: throw Seminar404("존재하지 않는 유저입니다.")
    }
}