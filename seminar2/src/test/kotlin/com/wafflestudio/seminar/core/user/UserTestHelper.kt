package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class UserTestHelper(
        private val passwordEncoder: PasswordEncoder,
        private val userRepository: UserRepository
) {
    fun createUser(
            email: String,
            username: String = "",
            password: String = "",


            ): UserEntity {
        val userEntity = UserEntity(email, username, passwordEncoder.encode(password), LocalDateTime.now(), participantProfile = null, instructorProfile = null)
        return userRepository.save(userEntity)
    }

    

}