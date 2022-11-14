package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.INSTRUCTOR
import com.wafflestudio.seminar.common.PARTICIPANT
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.join.UserSeminarRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.profile.dto.InstructorProfileResponse
import com.wafflestudio.seminar.core.profile.dto.ParticipantProfileRequest
import com.wafflestudio.seminar.core.profile.dto.ParticipantProfileResponse
import com.wafflestudio.seminar.core.seminar.dto.InstructingSeminarResponse
import com.wafflestudio.seminar.core.seminar.dto.ParticipantSeminarResponse
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.dto.UserRequest
import com.wafflestudio.seminar.core.user.dto.UserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserService {
    fun constructUserInformationById(userId: Long): UserResponse
    fun constructUserInformationByUser(user: UserEntity): UserResponse
    fun modifyUserInformation(userRequest: UserRequest, meUser: UserEntity)
    fun addToParticipantAndReturnUserInfo(
        participantProfileRequest: ParticipantProfileRequest,
        meUser: UserEntity
    ): UserResponse
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val authConfig: AuthConfig
) : UserService {
    private val encoder = authConfig.passwordEncoder()

    override fun constructUserInformationById(userId: Long): UserResponse {
        val user: UserEntity = userRepository.findByIdOrNull(userId)
            ?: throw UserException404("User $userId doesn't exists")
        return constructUserInformationByUser(user)
    }

    override fun constructUserInformationByUser(user: UserEntity): UserResponse {
        val participantProfileResponse: ParticipantProfileResponse? =
            user.participantProfile?.let {
                val userSeminarList = userSeminarRepository
                    .findAllByUserAndRole(user, PARTICIPANT)
                val seminarList = userSeminarList.map { us -> us.seminar }
                ParticipantProfileResponse(
                    id = it.id,
                    university = it.university,
                    isRegistered = it.isRegistered,
                    seminars = (userSeminarList zip seminarList).map { (us, s) ->
                        ParticipantSeminarResponse(
                            id = s.id,
                            name = s.name,
                            joinedAt = us.createdAt!!,
                            isActive = us.isActive,
                            droppedAt = us.droppedAt
                        )
                    }
                )
            }

        val instructorProfileResponse: InstructorProfileResponse? =
            user.instructorProfile?.let {
                val userSeminarList = userSeminarRepository
                    .findAllByUserAndRole(user, INSTRUCTOR)
                val seminarList = userSeminarList.map { us -> us.seminar }
                InstructorProfileResponse(
                    id = it.id,
                    company = it.company,
                    year = it.year,
                    instructingSeminars = (userSeminarList zip seminarList).map {
                        (us, s) ->
                        InstructingSeminarResponse(
                            id = s.id,
                            name = s.name,
                            joinedAt = us.createdAt!!,
                        )
                    }
                )
            }

        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            lastLogin = user.lastLogin!!,
            dateJoined = user.dataJoined!!,
            participant = participantProfileResponse,
            instructor = instructorProfileResponse,
        )
    }

    @Transactional
    override fun modifyUserInformation(userRequest: UserRequest, meUser: UserEntity) {
        val isParticipant: Boolean = meUser.participantProfile != null
        val isInstructor: Boolean = meUser.instructorProfile != null

        // Modify User Information
        if (userRequest.username.isNotBlank()) {
            meUser.username = userRequest.username
        }
        if (userRequest.password.isNotEmpty()) {
            meUser.password = encoder.encode(userRequest.password)
        }

        // Modify Participant Information
        if (isParticipant) {
            userRequest.university?.let { meUser.participantProfile!!.university = it }
        }

        // Modify Instructor Information
        if (isInstructor) {
            userRequest.company?.let { meUser.instructorProfile!!.company = it }

            if (userRequest.year != null) {
                if (userRequest.year <= 0) { throw UserException400("Not Appropriate Year given") } else { meUser.instructorProfile!!.year = userRequest.year }
            }
        }
    }

    override fun addToParticipantAndReturnUserInfo(
        participantProfileRequest: ParticipantProfileRequest,
        meUser: UserEntity
    ): UserResponse {
        if (meUser.participantProfile != null) {
            throw UserException409("Already participant.")
        }

        val participantProfile = ParticipantProfileEntity(
            user = meUser,
            university = participantProfileRequest.university,
            isRegistered = participantProfileRequest.isRegistered
        )
        meUser.participantProfile = participantProfile

        participantProfileRepository.save(participantProfile)
        userRepository.save(meUser)

        return constructUserInformationByUser(meUser)
    }
}