package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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
        return userRepository.save(
            UserEntity(
                username,
                email,
                passwordEncoder.encode(password),
                null,
                null,
                LocalDateTime.now()
            )
        )
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Long? = null,
    ): UserEntity {
        return userRepository.save(
            UserEntity(
                username,
                email,
                passwordEncoder.encode(password),
                InstructorProfileEntity(company, year as Int?),
                null,
                LocalDateTime.now()
            )
        )
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        return userRepository.save(
            UserEntity(
                username,
                email,
                passwordEncoder.encode(password),
                null,
                ParticipantProfileEntity(isActive, university),
                LocalDateTime.now()
            )
        )
    }
}