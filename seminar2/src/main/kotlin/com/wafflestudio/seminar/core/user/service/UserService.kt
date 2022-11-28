package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.database.InstructorRepository
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantRepository
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
    private val participantRepository: ParticipantRepository,
    private val instructorRepository: InstructorRepository,
): UserService {
    override fun getUser(userId: Long): User {
        return userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw Seminar404("존재하지 않는 userId 입니다")
    }

    override fun editUser(userId: Long, userRequest: UserRequest): User {
        val user = userRepository.findByIdOrNull(userId)!!
        
        user.participant?.let {
            user.participant!!.updateProfile(userRequest)
            participantRepository.save(user.participant!!)
        }
        user.instructor?.let {
            user.instructor!!.updateProfile(userRequest)
            instructorRepository.save(user.instructor!!)
        }
        userRepository.save(user)
        return user.toUser()
    }

    override fun registerToParticipate(userId: Long, participantRequest: ParticipantRequest): User {
        val user = userRepository.findByIdOrNull(userId)!!
        
        if (user.participant != null) {
            throw Seminar409("이미 참가자로 등록된 유저입니다")
        }
        
        val participant = ParticipantProfileEntity(
            user = user,
            university = participantRequest.university,
            isRegistered = participantRequest.isRegistered
        )
        participantRepository.save(participant)
        user.participant = participant
        userRepository.save(user)
        return user.toUser()
    }
}