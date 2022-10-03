package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.user.api.exception.*
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    config: AuthConfig
): UserService {
    private val encoder = config.passwordEncoder()

    override fun createUser(nickname: String, email: String, password: String) {
        if (nickname == "" || email == "" || password == "") throw UserException400("necessary info missing")
        if (userRepository.findByEmail(email) != null) throw UserException409("duplicate email")

        userRepository.save(UserEntity(nickname, email, encoder.encode(password)))
    }

    override fun login(email: String, password: String) {
        if (email == "" || password == "") throw UserException400("necessary info missing")
        if (!encoder.matches(
                password,
                userRepository.findByEmail(email)?.password ?: throw UserException401("wrong email or password")
            )
        ) {
            throw UserException401("wrong email or password")
        }
    }

}