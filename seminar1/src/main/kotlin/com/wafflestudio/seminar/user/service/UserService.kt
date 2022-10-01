package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.User406
import com.wafflestudio.seminar.user.api.request.User409
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    fun createUser(userRequest: CreateUserRequest) {
        valid(userRequest)
        val user = UserEntity(
            nickname = userRequest.nickname,
            email = userRequest.email,
            encodedPassword = passwordEncoder.encode(userRequest.password)
        )
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            throw User409("Duplicated email!")
        }
    }

    fun valid(userRequest: CreateUserRequest) {
        if (userRequest.nickname.isEmpty()) throw User406("Nickname is empty!")
        if (userRequest.email.isEmpty()) throw User406("Email is empty!")
        if (userRequest.password.isEmpty()) throw User406("Password is empty!")
    }
}