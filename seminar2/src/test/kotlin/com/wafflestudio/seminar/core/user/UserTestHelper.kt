package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class UserTestHelper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) {
    fun createUser(
        email: String,
        username: String = "",
        password: String = "",
        
    ): UserEntity {
        
        return userRepository.save(UserEntity(email, username, passwordEncoder.encode(password)))
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Long? = null,
    ): UserEntity {
        return userRepository.save(UserEntity.instructor(email, username, passwordEncoder.encode(password), company, year))
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        return userRepository.save(UserEntity.participant(email, username, passwordEncoder.encode(password), university, isActive))
    }
}