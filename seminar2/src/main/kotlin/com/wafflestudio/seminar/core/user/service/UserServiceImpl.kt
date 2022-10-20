package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository
) : UserService {

    override fun signUp(signUpRequest: SignUpRequest): Long {
        val userEntity = signUpRequest.toUserEntity()
        userRepository.save(userEntity)
        return userEntity.id
    }

    @Transactional(readOnly = true)
    override fun findOne(userId: Long): User {
        val userEntity = userRepository.findById(userId).get()
        return userEntity.toDTO()
    }

    override fun editProfile(userId: Long, editProfileRequest: EditProfileRequest) {
        val userEntity = userRepository.findById(userId).get()
        userEntity.email = editProfileRequest.email
        userEntity.username = editProfileRequest.username
        userEntity.password = editProfileRequest.password
        if (userEntity.participantProfile != null) {
            userEntity.participantProfile!!.university = editProfileRequest.university
        }
        if (userEntity.instructorProfile != null) {
            userEntity.instructorProfile!!.company = editProfileRequest.company
            userEntity.instructorProfile!!.year = editProfileRequest.year
        }
    }

    override fun beParticipant(userId: Long, participantRequest: ParticipantRequest) {
        val userEntity = userRepository.findById(userId).get()
        if (userEntity.participantProfile != null) {
            throw Seminar409("You have participant profile already")
        }
        val participantProfileEntity =
            ParticipantProfileEntity(participantRequest.university, participantRequest.isRegisterd)
        participantProfileEntity.addUser(userEntity)
        participantProfileRepository.save(participantProfileEntity)
    }
}