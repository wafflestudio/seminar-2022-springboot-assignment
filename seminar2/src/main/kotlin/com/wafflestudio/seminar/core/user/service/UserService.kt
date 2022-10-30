package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.jointable.UserSeminarRepository
import com.wafflestudio.seminar.core.profile.dto.InstructorProfileResponse
import com.wafflestudio.seminar.core.profile.dto.ParticipantProfileResponse
import com.wafflestudio.seminar.core.seminar.dto.InstructingSeminarResponse
import com.wafflestudio.seminar.core.seminar.dto.ParticipantSeminarResponse
import com.wafflestudio.seminar.core.user.api.dto.UserResponse
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    fun constructUserInformationById(userId: Long): UserResponse
    fun constructUserInformationByUser(user: UserEntity): UserResponse
}

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val userSeminarRepository: UserSeminarRepository,
): UserService {
    override fun constructUserInformationById(userId: Long): UserResponse {
        val user: UserEntity = userRepository.findByIdOrNull(userId)
                ?: throw UserException404("User ${userId} doesn't exists")
        return constructUserInformationByUser(user)
    }

    override fun constructUserInformationByUser(user: UserEntity): UserResponse {
        val participantProfileResponse: ParticipantProfileResponse? = 
                user.participantProfile?.let {
                    val userSeminarList = userSeminarRepository
                                    .findAllByUserAndIsParticipant(user, true)
                    val seminarList = userSeminarList.map { us -> us.seminar }
                    ParticipantProfileResponse(
                            id = it.id,
                            university = it.university,
                            isRegistered = it.isRegistered,
                            seminars = (userSeminarList zip seminarList).map { (us, s) -> ParticipantSeminarResponse(
                                    id = s.id,
                                    name = s.name,
                                    joinedAt = us.createdAt!!,
                                    isActive = us.isActive,
                                    droppedAt = us.droppedAt
                            ) }
                    )
                }
        
        val instructorProfileResponse: InstructorProfileResponse? = 
                user.instructorProfile?.let {
                    val userSeminarList = userSeminarRepository
                            .findAllByUserAndIsParticipant(user, false)
                    val seminarList = userSeminarList.map { us -> us.seminar }
                    InstructorProfileResponse(
                            id = it.id,
                            company = it.company,
                            year = it.year,
                            instructingSeminars = (userSeminarList zip seminarList).map { 
                                (us, s) -> InstructingSeminarResponse(
                                    id = s.id,
                                    name = s.name,
                                    joinedAt = us.createdAt!!,
                            ) }
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
}