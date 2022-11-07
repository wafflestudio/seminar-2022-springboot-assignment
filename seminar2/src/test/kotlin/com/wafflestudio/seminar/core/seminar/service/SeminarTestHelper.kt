package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.INSTRUCTOR
import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.join.UserSeminarRepository
import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
internal class SeminarTestHelper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository
) {
    fun createUser(
        email: String,
        username: String = "",
        password: String = "",
    ): UserEntity {
        return UserEntity(email = email, username = username, password = passwordEncoder.encode(password))
    }

    fun createInstructor(
        email: String,
        username: String = "",
        password: String = "",
        company: String = "",
        year: Int? = null,
    ): UserEntity {
        val newUser = createUser(username = username, email = email, password = password)
        val instructorProfileEntity = InstructorProfileEntity(user = newUser, company = company, year = year)
        newUser.instructorProfile = instructorProfileEntity

        instructorProfileRepository.save(instructorProfileEntity)
        return userRepository.save(newUser)
    }

    fun createParticipant(
        email: String,
        username: String = "",
        password: String = "",
        university: String = "",
        isActive: Boolean = true,
    ): UserEntity {
        val newUser = createUser(username = username, email = email, password = password)
        val participantProfileEntity =
            ParticipantProfileEntity(user = newUser, university = university, isRegistered = isActive)
        newUser.participantProfile = participantProfileEntity

        participantProfileRepository.save(participantProfileEntity)
        return userRepository.save(newUser)
    }
    
    
    @Transactional
    fun createSeminar(
        instructor: UserEntity,
        name: String = "",
        capacity: Int = 100,
        count: Int = 10,
        time: Int = 150,
        online:Boolean = false
    ): SeminarEntity {
        
        val seminar = seminarRepository.save(
                SeminarEntity(
                    name = name,
                    capacity = capacity, count = count,
                    time = time, online = online,
                    users = mutableSetOf(),
                    created_user = instructor
                )
            )
        
        val userSeminar = userSeminarRepository.save(
            UserSeminarEntity(
                user = instructor, seminar = seminar, role = INSTRUCTOR
            )
        )
                
        return seminar.apply { this.users.add(userSeminar) }
    }

    
    
    
    @Transactional
    fun joinSeminar(
        user: UserEntity,
        seminar: SeminarEntity,
        role: String,
        isActive: Boolean = true
    ) {
        val userSeminar = userSeminarRepository.save(
            UserSeminarEntity(user, seminar, isActive, role = role)
        )
        seminar.users.add(userSeminar)
    }
}