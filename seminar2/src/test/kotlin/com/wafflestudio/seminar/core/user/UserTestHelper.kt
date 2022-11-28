package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.InstructorEntity
import com.wafflestudio.seminar.core.user.database.ParticipantEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
internal class UserTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
) {
    fun deleteAllUser() {
        userRepository.deleteAll()
    }
    
    fun createUser(
        email: String,
        username: String = "",
        password: String = "",
    ): UserEntity {
        return userRepository.save(UserEntity(username = username, email = email, password = password))
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val instructorEntity = InstructorEntity(company, year)
        return userRepository.save(UserEntity(username, email, password, instructor = instructorEntity))
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        val participantEntity = ParticipantEntity(university, isActive)
        return userRepository.save(UserEntity(username, email, password, participant = participantEntity))
    }
}