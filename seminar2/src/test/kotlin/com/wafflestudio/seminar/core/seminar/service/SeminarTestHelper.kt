package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SeminarTestHelper @Autowired constructor(
        private val userRepository: UserRepository,
        private val seminarRepository: SeminarRepository,
        private val userSeminarRepository: UserSeminarRepository,
) {
    fun createUser(
            email: String,
            username: String,
            password: String,
            role: RoleType,
    ) = UserEntity(
            email, username, password, role
    ).also { userRepository.save(it) }

    fun createSeminar(
            name: String,
            instructor: String,
            capacity: Long,
            count: Long,
            time: String,
            online: Boolean,
    ) = SeminarEntity(
            name, instructor, capacity, count, time, online
    ).also { seminarRepository.save(it) }

    fun createUserSeminarEntity(
            user: UserEntity,
            seminar: SeminarEntity,
    ) = UserSeminarEntity(user, seminar)
            .also { userSeminarRepository.save(it) }
}