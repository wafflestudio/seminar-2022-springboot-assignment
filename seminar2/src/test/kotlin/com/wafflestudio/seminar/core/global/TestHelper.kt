package com.wafflestudio.seminar.core.global

import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.type.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.transaction.Transactional

@Component
internal class TestHelper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val userSeminarRepository: UserSeminarRepository,
) {

    fun createUser(
        loginedAt: LocalDateTime = LocalDateTime.now(),
        username: String = "username",
        email: String,
        password: String = "password",
        role: UserRole,
        isRegistered: Boolean = true
    ): UserEntity {
        return userRepository.save(
            UserEntity(
                loginedAt, username, email,
                passwordEncoder.encode(password), role, isRegistered = isRegistered
            )
        )
    }

    fun createInstructor(
        loginedAt: LocalDateTime = LocalDateTime.now(),
        username: String = "username",
        email: String,
        password: String = "password",
        company: String = "",
        year: Int? = null,
        isRegistered: Boolean = true
    ): UserEntity {
        val user = userRepository.save(
            UserEntity(
                loginedAt, username, email,
                passwordEncoder.encode(password), UserRole.INSTRUCTOR, company = company, year = year, isRegistered = isRegistered
            )
        )
        instructorProfileRepository.save(InstructorProfileEntity(user))
        return user
    }

    fun createParticipant(
        loginedAt: LocalDateTime = LocalDateTime.now(),
        username: String = "username",
        email: String,
        password: String = "password",
        university: String = "",
        isRegistered: Boolean = true,
    ): UserEntity {
        val user = userRepository.save(
            UserEntity(
                loginedAt, username, email,
                passwordEncoder.encode(password), UserRole.PARTICIPANT, university = university, isRegistered = isRegistered
            )
        )
        participantProfileRepository.save(ParticipantProfileEntity(user))
        return user
    }

    fun createSeminar(
        userEntity: UserEntity,
        name: String,
        capacity: Int = 10000,
        count: Int = 10000,
        time: String = "11:11",
        online: Boolean = true,
    ): SeminarEntity {
        val seminar = seminarRepository.save(SeminarEntity(name, capacity, count, time, online))
        userSeminarRepository.save(UserSeminarEntity(user = userEntity, seminar = seminar, isParticipant = false))
        return seminar
    }

    fun createUserSeminar(
        userEntity: UserEntity,
        seminarEntity: SeminarEntity,
    ) : UserSeminarEntity {
        return userSeminarRepository.save(UserSeminarEntity(user = userEntity, seminar = seminarEntity, isParticipant = userEntity.role == UserRole.PARTICIPANT))
    }
}