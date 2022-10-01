package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.*
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    fun createUser(request: CreateUserRequest) {
        valid(request)
        val user = UserEntity(
            nickname = request.nickname,
            email = request.email,
            encodedPassword = passwordEncoder.encode(request.password)
        )
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            throw User409("Duplicated email!")
        }
    }

    fun valid(request: CreateUserRequest) {
        if (request.nickname.isEmpty()) throw User406("Nickname is empty!")
        if (request.email.isEmpty()) throw User406("Email is empty!")
        if (request.password.isEmpty()) throw User406("Password is empty!")
    }

    fun login(request: LoginRequest): String {
        try {
            val user = userRepository.findByEmail(request.email)
            if (!passwordEncoder.matches(request.password, user.encodedPassword)) {
                throw User401("Wrong password!")
            }
            return user.nickname
        } catch (e: EmptyResultDataAccessException) {
            throw User404("Email not found!")
        }
    }
}