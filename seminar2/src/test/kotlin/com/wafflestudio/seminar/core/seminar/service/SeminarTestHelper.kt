package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.profile.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantProfile
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SeminarTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
) {
    @Transactional
    fun createInstructor(
        email: String,
        username: String,
        password: String,
        company: String,
        year: Int,
    ): UserEntity {
        val user = createUser(email, username, password, RoleType.INSTRUCTOR)
        user.instructorProfile = createInstructorProfile(company, year, user)
        return userRepository.save(user)
    }

    @Transactional
    fun createParticipant(
        email: String,
        username: String,
        password: String,
        university: String,
        isRegistered: Boolean,
    ): UserEntity {
        val user = createUser(email, username, password, RoleType.PARTICIPANT)
        user.participantProfile = createParticipantProfile(university, isRegistered, user)
        return userRepository.save(user)
    }


    fun createUser(
        email: String,
        username: String,
        password: String,
        role: RoleType,
    ) = UserEntity(email, username, password, role)
        .let { userRepository.save(it) }

    fun createInstructorProfile(
        company: String,
        year: Int,
        user: UserEntity,
    ) = InstructorProfile(company, year, user)

    fun createParticipantProfile(
        university: String,
        isRegistered: Boolean,
        user: UserEntity,
    ) = ParticipantProfile(university, isRegistered, user)

    
    @Transactional
    fun createSeminar(
        name: String,
        instructor: UserEntity,
        capacity: Long,
        count: Long,
        time: String,
        online: Boolean,
    ): SeminarEntity {
        val seminar = SeminarEntity(name, instructor.username, capacity, count, time, online)
        UserSeminarEntity(instructor, seminar).run {
            seminar.userSeminarList = mutableListOf(this)
            instructor.seminarList = mutableListOf(this)
        }
        //seminar.userSeminarList = mutableListOf(UserSeminarEntity(instructor, seminar))
        
        return seminarRepository.save(seminar)
    }

    @Transactional
    fun createUserSeminarEntity(
        user: UserEntity,
        seminar: SeminarEntity,
    ) {
        user.seminarList!!.add(UserSeminarEntity(user, seminar))
        userRepository.save(user)  // 이렇게 해야 위 userSeminarEntity 정보가 repository에 저장되는 듯하다
    }
}