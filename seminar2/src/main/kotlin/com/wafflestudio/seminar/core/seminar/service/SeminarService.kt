package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepositorySupport
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

interface SeminarService {
    fun makeSeminar(userId: Long, req: SeminarDto.SeminarRequest): SeminarDto.SeminarProfileResponse
    fun updateSeminar(userId: Long, req: SeminarDto.UpdateSeminarRequest): SeminarDto.SeminarProfileResponse
    fun getSeminarById(seminarId: Long): SeminarDto.SeminarProfileResponse
    fun getSeminars(name: String?, earliest: String?): List<SeminarDto.SeminarProfileSimplifiedResponse>
    fun participateSeminar(seminarId: Long, role: UserDto.Role, userId: Long): SeminarDto.SeminarProfileResponse
    fun dropSeminar(seminarId: Long, userId: Long): SeminarDto.SeminarProfileResponse
}

@Service
class SeminarServiceImpl(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val seminarRepositorySupport: SeminarRepositorySupport,
    private val userRepositorySupport: UserRepositorySupport,
) : SeminarService {


    @Transactional
    override fun makeSeminar(userId: Long, req: SeminarDto.SeminarRequest): SeminarDto.SeminarProfileResponse {
        val optionalUserEntity: Optional<UserEntity> = userRepository.findById(userId)
        val userEntity: UserEntity = optionalUserEntity.get()
        if (userEntity.instructorProfileEntity == null) {
            throw Seminar403("Only instructor can make a seminar.")
        }
        for (userSeminarEntity in userEntity.userSeminarEntities) {
            if (userSeminarEntity.role == UserDto.Role.INSTRUCTOR) {
                throw Seminar400("You are already conducting the other seminar. You can not conduct this seminar.")
            }
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        try {
            val dateTime = LocalTime.parse(req.time, formatter)
        } catch (e: DateTimeParseException) {
            throw Seminar400("'time' should be written as a format 'HH:mm'.")
        }
        val seminarEntity = SeminarEntity(
            name = req.name!!,
            capacity = req.capacity!!,
            count = req.count!!,
            time = req.time!!,
            online = req.online ?: true,
            managerId = userId
        )
        val userSeminarEntity = UserSeminarEntity(
            role = UserDto.Role.INSTRUCTOR,
            isActive = true,
            userEntity = userEntity,
            seminarEntity = seminarEntity,
        )
        seminarRepository.save(seminarEntity).id
        userSeminarRepository.save(userSeminarEntity)
        return seminarEntity.toDto()
    }

    @Transactional
    override fun updateSeminar(userId: Long, req: SeminarDto.UpdateSeminarRequest): SeminarDto.SeminarProfileResponse {
        val seminarEntity: SeminarEntity = seminarRepository.findByManagerId(userId)
            ?: throw Seminar403("You don't conduct any seminar. Thus you can not update a seminar.")
        if (req.time != null) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            try {
                val dateTime = LocalTime.parse(req.time, formatter)
            } catch (e: DateTimeParseException) {
                throw Seminar400("'time' should be written as a format 'HH:mm'.")
            }
        }
        req.name?.let { seminarEntity.name = it }
        req.capacity?.let { seminarEntity.capacity = it }
        req.count?.let { seminarEntity.count = it }
        req.time?.let { seminarEntity.time = it }
        req.online?.let { seminarEntity.online = it }
        seminarEntity.modifiedAt = LocalDateTime.now()
        seminarRepository.save(seminarEntity).id
        return seminarEntity.toDto()
    }

    @Transactional
    override fun getSeminarById(seminarId: Long): SeminarDto.SeminarProfileResponse {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404("This seminar doesn't exist.")
        return seminar.toDto()
    }

    private fun SeminarEntity.toDto(): SeminarDto.SeminarProfileResponse {
        val userSeminars = userSeminarRepository.findAllBySeminarEntity_Id(id)
        val (instructors, participants) = userSeminars!!.partition { us -> us.isInstructor }

        val instructorProjections = seminarRepositorySupport.findInstructorProjections(instructors.map { it.id })
        val participantProjections = seminarRepositorySupport.findParticipantProjections(participants.map { it.id })

        return SeminarDto.SeminarProfileResponse.of(this, instructorProjections, participantProjections)
    }

    @Transactional
    override fun getSeminars(
        name: String?,
        earliest: String?
    ): List<SeminarDto.SeminarProfileSimplifiedResponse> {
        val isAscending: Boolean = earliest.equals("earliest")
        val seminars = seminarRepositorySupport.getSeminarList(name, isAscending)

        val userIds = seminars.flatMap { it.userSeminarEntities.map { it.userEntity.id } }
        val userMap = userRepositorySupport.findAllWithUserIds(userIds).associateBy { it.id }

        return seminars.map { seminar ->
            val userSeminars = seminar.userSeminarEntities
            val (instructors, participants) = userSeminars.partition { it.isInstructor }
            val instructorProjections = instructors.map { inst ->
                UserDto.SeminarInstructorProfileResponse.of(
                    userMap[inst.userEntity.id]!!,
                    inst
                )
            }
            SeminarDto.SeminarProfileSimplifiedResponse.of(seminar, instructorProjections)
        }
    }

    @Transactional
    override fun participateSeminar(
        seminarId: Long,
        role: UserDto.Role,
        userId: Long
    ): SeminarDto.SeminarProfileResponse {
        val seminarEntity =
            seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404("This seminar doesn't exist.")
        val userEntity = userRepository.findById(userId).get()
        val userSeminar = userSeminarRepository.findByUserEntity_Id(userId)

        if (userSeminar != null) {
            if (userSeminar.isActive)
                throw Seminar400("You are already in this seminar as a " + userSeminar.role + ".")
            else
                throw Seminar400("You dropped this seminar before. You can not participate in this seminar again.")
        }
        
        if (role == UserDto.Role.PARTICIPANT) {
            if (userEntity.participantProfileEntity == null) {
                throw Seminar403("Only participant can participate in a seminar")
            }
            if (!userEntity.participantProfileEntity!!.isRegistered) {
                throw Seminar403("You should register to participate in a seminar.")
            }
            if (seminarEntity.capacity <= seminarEntity.participantCount) {
                throw Seminar400("This seminar is full. You can not participate in this seminar.")
            }
            seminarEntity.participantCount += 1L
            seminarRepository.save(seminarEntity)
        } else {
            if (userEntity.instructorProfileEntity == null) {
                throw Seminar403("Only instructor can conduct a seminar.")
            }
            for (userSeminarEntity in userEntity.userSeminarEntities) {
                if (userSeminarEntity.role == UserDto.Role.INSTRUCTOR) {
                    throw Seminar400("You are already conducting the other seminar. You can not conduct this seminar.")
                }
            }

        }
        val userSeminarEntity = UserSeminarEntity(
            role = role,
            isActive = true,
            userEntity = userEntity,
            seminarEntity = seminarEntity,
        )
        userSeminarRepository.save(userSeminarEntity)
        return seminarEntity.toDto()
    }

    @Transactional
    override fun dropSeminar(seminarId: Long, userId: Long): SeminarDto.SeminarProfileResponse {
        val seminarEntity =
            seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404("This seminar doesn't exist.")
        val userEntity = userRepository.findById(userId)!!.get()
        if (userEntity.role == UserDto.Role.INSTRUCTOR) {
            throw Seminar403("Instructor can not drop the seminar.")
        }
        val userSeminarEntities = userSeminarRepository.findAllBySeminarEntity_Id(seminarId)
        val userSeminarEntity = userSeminarEntities!!.find { it.userEntity.id == userId } ?: return seminarEntity.toDto()

        userSeminarEntity.isActive = false
        userSeminarEntity.droppedAt = LocalDateTime.now()
        userSeminarEntity.modifiedAt = LocalDateTime.now()
        seminarEntity.participantCount -= 1L
        userSeminarRepository.save(userSeminarEntity)

        return seminarEntity.toDto()
    }

}