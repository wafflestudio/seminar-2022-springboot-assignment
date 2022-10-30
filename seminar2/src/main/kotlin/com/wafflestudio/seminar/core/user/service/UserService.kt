package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    fun getUser(userId: Long): User
    fun editUser(userId: Long, userRequest: UserRequest): User
    fun registerToParticipate(userId: Long, participantRequest: ParticipantRequest): User
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
): UserService {
    override fun getUser(userId: Long): User {
        return userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw Seminar404("존재하지 않는 userId 입니다")
    }

    override fun editUser(userId: Long, userRequest: UserRequest): User {
        TODO("Not yet implemented")
    }

    override fun registerToParticipate(userId: Long, participantRequest: ParticipantRequest): User {
        TODO("Not yet implemented")
    }
}