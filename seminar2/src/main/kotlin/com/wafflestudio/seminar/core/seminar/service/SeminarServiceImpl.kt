package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar200
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.repository.CustomSeminarRepository
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.userSeminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.repository.CustomUserRepository
import com.wafflestudio.seminar.core.userSeminar.repository.CustomUserSeminarRepository
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.core.userSeminar.repository.UserSeminarRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val customUserRepository: CustomUserRepository,
    private val customSeminarRepository: CustomSeminarRepository,
    private val customUserSeminarRepository: CustomUserSeminarRepository
) : SeminarService {
    override fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): Seminar {
        val userEntity =
            customUserRepository.findByIdWithUserSeminar(userId)
                ?: throw Seminar404("No existing user with id: ${userId}")
        if (userEntity.instructorProfile == null) {
            throw Seminar403("Cannot create seminar. You don't have instructor profile")
        }
        for (userSeminar in userEntity.userSeminars) {
            if (userSeminar.role == Role.INSTRUCTOR) {
                throw Seminar400("You are already instructing a seminar")
            }
        }
        val seminarEntity = SeminarEntity(
            createSeminarRequest.name!!,
            createSeminarRequest.capacity!!,
            createSeminarRequest.count!!,
            LocalTime.parse(createSeminarRequest.time),
            createSeminarRequest.online,
            userId
        )
        val userSeminarEntity = UserSeminarEntity(userEntity, seminarEntity, Role.INSTRUCTOR)
        seminarEntity.addUserSeminar(userSeminarEntity)
        seminarRepository.save(seminarEntity)
        userSeminarRepository.save(userSeminarEntity)
        return seminarEntity.toDTO()
    }

    override fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): Seminar {
        val seminarEntity = customSeminarRepository.findByIdWithUserSeminarAndUser(editSeminarRequest.id!!)
            ?: throw Seminar404("No existing seminar with id: ${editSeminarRequest.id}")
        if (seminarEntity.creatorId != userId) {
            throw Seminar403("You are not the creator of this seminar")
        }
        if (editSeminarRequest.name != null) {
            seminarEntity.name = editSeminarRequest.name
        }
        if (editSeminarRequest.capacity != null) {
            seminarEntity.capacity = editSeminarRequest.capacity
        }
        if (editSeminarRequest.count != null) {
            seminarEntity.count = editSeminarRequest.count
        }
        if (editSeminarRequest.time != null) {
            seminarEntity.time = LocalTime.parse(editSeminarRequest.time)
        }
        if (editSeminarRequest.online != null) {
            seminarEntity.online = editSeminarRequest.online
        }
        return seminarEntity.toDTO()
    }

    @Transactional(readOnly = true)
    override fun getSeminar(seminarId: Long): Seminar {
        val seminarEntity = customSeminarRepository.findByIdWithUserSeminarAndUser(seminarId)
            ?: throw Seminar404("No existing seminar with id: ${seminarId}")
        return seminarEntity.toDTO()
    }

    @Transactional(readOnly = true)
    override fun getSeminars(name: String, order: String): List<SeminarResponse> {
        val seminarEntities = customSeminarRepository.findByNameAndOrder(name, order)
        val seminars = ArrayList<SeminarResponse>()
        for (seminarEntity in seminarEntities) {
            seminars.add(seminarEntity.toSeminarResponse())
        }
        return seminars
    }

    override fun joinSeminar(userId: Long, seminarId: Long, role: Role): Seminar {
        val seminarEntity = customSeminarRepository.findByIdWithUserSeminarAndUser(seminarId)
            ?: throw Seminar404("No existing seminar with id: ${seminarId}")
        val userEntity =
            customUserRepository.findByIdWithUserSeminar(userId)
                ?: throw throw Seminar404("No existing user with id: ${userId}")
        when (role) {
            Role.PARTICIPANT -> {
                if (userEntity.participantProfile == null) {
                    throw Seminar403("You don't have participant profile")
                }
                if (!userEntity.participantProfile!!.isRegistered) {
                    throw Seminar403("You are not registered")
                }
                if (seminarEntity.capacity == seminarEntity.toSeminarResponse().participantCount) {
                    throw Seminar400("Sorry, this seminar is full")
                }
            }

            Role.INSTRUCTOR -> {
                if (userEntity.instructorProfile == null) {
                    throw Seminar403("You don't have instructor profile")
                }
                for (userSeminar in userEntity.userSeminars) {
                    if (userSeminar.role == Role.INSTRUCTOR) {
                        throw Seminar400("You are already instructing a seminar")
                    }
                }
            }
        }
        for (userSeminar in userEntity.userSeminars) {
            if (userSeminar.seminar == seminarEntity && userSeminar.isActive) {
                throw Seminar400("You already joined this seminar")
            } else if (userSeminar.seminar == seminarEntity && !userSeminar.isActive) {
                throw Seminar400("You cannot join again after dropping seminar")
            }
        }
        val userSeminar = UserSeminarEntity(userEntity, seminarEntity, role)
        seminarEntity.addUserSeminar(userSeminar)
        userEntity.addUserSeminar(userSeminar)
        userSeminarRepository.save(userSeminar)
        return seminarEntity.toDTO()
    }

    override fun dropSeminar(userId: Long, seminarId: Long): Seminar {
        val seminarEntity = customSeminarRepository.findByIdWithUserSeminarAndUser(seminarId)
            ?: throw Seminar404("No existing seminar with id: ${seminarId}")
        val userSeminarEntity =
            customUserSeminarRepository.findByUserIdAndSeminarId(userId, seminarId) ?: throw Seminar200("")
        if (userSeminarEntity.role == Role.INSTRUCTOR) {
            throw Seminar403("Instructor cannot drop seminar")
        }
        userSeminarEntity.isActive = false
        userSeminarEntity.droppedAt = LocalDateTime.now()
        return seminarEntity.toDTO()
    }
}