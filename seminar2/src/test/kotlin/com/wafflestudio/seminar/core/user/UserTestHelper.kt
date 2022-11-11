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
        return userRepository.save(UserEntity(email, username, password, LocalDate.now()))
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val instructor = InstructorProfileEntity(company, year)
        return userRepository.save(UserEntity(username, email, password, LocalDate.now(), instructor = instructor))
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        val participant = ParticipantProfileEntity(university, isActive)
        return userRepository.save(UserEntity(username, email, password, LocalDate.now(), participant = participant))
    }
}