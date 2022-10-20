package com.wafflestudio.seminar.core.user.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val customSeminarRepository: CustomSeminarRepository
) : UserService {

    override fun signUp(signUpRequest: SignUpRequest): Long {
        val userEntity = signUpRequest.toUserEntity()
        userRepository.save(userEntity)
        return userEntity.id
    }

    @Transactional(readOnly = true)
    override fun getMyProfile(userId: Long): User {
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

    override fun createSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar {
        val userEntity = userRepository.findById(userId).get()
        val seminarEntity = SeminarEntity(
            seminarRequest.name,
            seminarRequest.capacity,
            seminarRequest.count,
            seminarRequest.time,
            seminarRequest.online
        )
        val userSeminarEntity = UserSeminarEntity(userEntity, seminarEntity, Role.INSTRUCTOR)
        seminarEntity.addUserSeminar(userSeminarEntity)
        seminarRepository.save(seminarEntity)
        userSeminarRepository.save(userSeminarEntity)
        return seminarEntity.toDTO()
    }

    override fun editSeminar(seminarId: Long, seminarRequest: SeminarRequest): Seminar {
        val seminarEntity = seminarRepository.findById(seminarId).get()
        seminarEntity.name = seminarRequest.name
        seminarEntity.capacity = seminarRequest.capacity
        seminarEntity.count = seminarRequest.count
        seminarEntity.time = seminarRequest.time
        seminarEntity.online = seminarRequest.online
        return seminarEntity.toDTO()
    }

    override fun getSeminar(seminarId: Long): Seminar {
        val seminarEntity = seminarRepository.findById(seminarId).get()
        return seminarEntity.toDTO()
    }

    override fun getSeminars(name: String, order: String): List<SeminarResponse> {
        val seminarEntities = customSeminarRepository.findByNameWithOrder("pr", "earliest")
        val seminars = ArrayList<SeminarResponse>()
        for (seminarEntity in seminarEntities) {
            seminars.add(seminarEntity.toSeminarResponse())
        }
        return seminars
    }

}