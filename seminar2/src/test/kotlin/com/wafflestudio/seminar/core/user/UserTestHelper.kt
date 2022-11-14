package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class UserTestHelper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val instructorRepository: InstructorRepository,
    private val participantRepository: ParticipantRepository
) {
    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isRegistered: Boolean = true
    ): UserEntity {
        val user = UserEntity(
            email = email, username = username, password = passwordEncoder.encode(password), seminars = mutableListOf()
        )
        userRepository.save(user)
        val participantProfileEntity = ParticipantProfileEntity(
            user = user, university = university, isRegistered = isRegistered
        )
        participantRepository.save(participantProfileEntity)
        user.participant = participantProfileEntity
        return userRepository.save(user)
    }

    fun createInstructor(
        email: String, username: String = "", password: String = "", company: String = "", year: Int? = null
    ): UserEntity {
        val user = UserEntity(
            email = email, username = username, password = passwordEncoder.encode(password), seminars = mutableListOf()
        )
        userRepository.save(user)
        val instructorProfileEntity = InstructorProfileEntity(
            user = user, company = company, year = year
        )
        instructorRepository.save(instructorProfileEntity)
        user.instructor = instructorProfileEntity
        userRepository.save(user)
        return user
    }
}