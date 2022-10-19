package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val modelMapper: ModelMapper
) : UserService {

    @Transactional
    override fun register(signUpRequest: SignUpRequest): Long {
        val userEntity = modelMapper.map(signUpRequest, UserEntity::class.java)
        if (signUpRequest.role == Role.PARTICIPANT) {
            val participantProfileEntity = ParticipantProfileEntity(
                signUpRequest.university, signUpRequest.isRegistered
            )
            participantProfileEntity.addUser(userEntity)
        } else {
            val instructorProfileEntity = InstructorProfileEntity(
                signUpRequest.company, signUpRequest.year
            )
            instructorProfileEntity.addUser(userEntity)
        }
        userRepository.save(userEntity)
        return userEntity.id
    }

    @Transactional(readOnly = true)
    override fun findOne(userId: Long): User {
        TODO()
    }

    override fun update(): Long {
        TODO("Not yet implemented")
    }
}