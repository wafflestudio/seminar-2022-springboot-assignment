package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
internal class UserTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
) {
    fun createUser(
        email: String,
        username: String = "",
        password: String = "",
    ): UserEntity {
        return userRepository.save(UserEntity(email, username, password))
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val user = UserEntity(username, email, password)
        val instructor = InstructorProfileEntity(user, company, year)
        user.instructor = instructor
        return userRepository.save(user)
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isRegistered: Boolean = true,
    ): UserEntity {
        val user = UserEntity(username, email, password)
        val participant = ParticipantProfileEntity(user, university, isRegistered)
        user.participant = participant
        return userRepository.save(user)
    }
}