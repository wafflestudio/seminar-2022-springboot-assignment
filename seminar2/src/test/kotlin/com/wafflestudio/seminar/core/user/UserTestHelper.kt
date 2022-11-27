package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class UserTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
) {
    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val instructor = InstructorProfileEntity(company = company, year = year)
        return userRepository.save(
            UserEntity(
                email,
                username,
                password,
                LocalDateTime.now(),
                instructorProfile = instructor
            )
        )
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isRegistered: Boolean = true,
    ): UserEntity {
        val participant = ParticipantProfileEntity(university = university, isRegistered = isRegistered)
        return userRepository.save(
            UserEntity(
                email,
                username,
                password,
                LocalDateTime.now(),
                participantProfile = participant
            )
        )
    }
}