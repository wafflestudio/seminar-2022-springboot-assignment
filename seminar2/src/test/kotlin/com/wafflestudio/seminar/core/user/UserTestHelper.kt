package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class UserTestHelper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
) {
    fun createUser(
        email: String,
        username: String = "",
        password: String = "",
    ): UserEntity {
        return userRepository.save(
            UserEntity(
                email,
                username,
                passwordEncoder.encode(password),
            )
        )
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val instructor = createUser(email, username, password)
        instructor.instructorProfile = instructorProfileRepository.save(
            InstructorProfileEntity(company, year)
        )
        
        return userRepository.save(instructor)
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        val participant = createUser(email, username, password)
        participant.participantProfile = participantProfileRepository.save(
            ParticipantProfileEntity(university, isActive)
        )
        
        return userRepository.save(participant)
    }
}