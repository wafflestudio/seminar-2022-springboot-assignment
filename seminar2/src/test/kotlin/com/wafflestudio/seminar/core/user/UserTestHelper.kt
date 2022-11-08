package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.database.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class UserTestHelper(
        private val passwordEncoder: PasswordEncoder,
        private val userRepository: UserRepository,
        private val instructorProfileRepository: InstructorProfileRepository,
        private val participantProfileRepository: ParticipantProfileRepository
) {
    fun createUser(
            email: String,
            username: String = "",
            password: String = "",


            ): UserEntity {
        val userEntity = UserEntity(email, username, passwordEncoder.encode(password), LocalDateTime.now(), participantProfile = null, instructorProfile = null)
        return userRepository.save(userEntity)
    }

    fun createInstructorUser(
            email: String,
            username: String = "",
            password: String = "",
            role: String = "INSTRUCTOR",
            company: String = "",
            year1: Int = 0,

    ): UserEntity {
        val userEntity = UserEntity(email, username, passwordEncoder.encode(password), LocalDateTime.now(),participantProfile = null, instructorProfile = null)
        val instructorProfile = InstructorProfileEntity(userEntity, company, year1)
        userEntity.instructorProfile = instructorProfile
        instructorProfileRepository.save(instructorProfile)
        return userRepository.save(userEntity)
    }


    fun createParticipantUser(
            email: String,
            username: String = "",
            password: String = "",
            role: String = "PARTICIPANT",
            university: String = "",
            isRegistered: Boolean = true,

            ): UserEntity {
        val userEntity = UserEntity(email, username, passwordEncoder.encode(password), LocalDateTime.now(),participantProfile = null, instructorProfile = null)
        val participantProfile = ParticipantProfileEntity(userEntity,university, isRegistered)
        userEntity.participantProfile = participantProfile
        participantProfileRepository.save(participantProfile)
        return userRepository.save(userEntity)
    }
    
    

}