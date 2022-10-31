package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepositorySupport
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarRepository
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
    fun getSeminars(name: String?, earliest: String?): MutableList<SeminarDto.SeminarProfileSimplifiedResponse>
    fun participateSeminar(seminarId: Long, role: String, userId: Long): SeminarDto.SeminarProfileResponse
    fun dropSeminar(seminarId: Long, userId: Long): SeminarDto.SeminarProfileResponse
}

@Service
class SeminarServiceImpl(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val seminarRepositorySupport: SeminarRepositorySupport
) : SeminarService {

    @Transactional
    override fun makeSeminar(userId: Long, req: SeminarDto.SeminarRequest): SeminarDto.SeminarProfileResponse {
        val optionalUserEntity: Optional<UserEntity> = userRepository.findById(userId)
        val userEntity: UserEntity = optionalUserEntity.get()
        if (userEntity.instructorProfileEntity == null) {
            throw Seminar403("Only instructor can make a seminar.")
        }
        for (userSeminarEntity in userEntity.userSeminarEntities) {
            if (userSeminarEntity.role == "INSTRUCTOR") {
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
            role = "INSTRUCTOR",
            isActive = true,
            userEntity = userEntity,
            seminarEntity = seminarEntity,
        )
        val seminarId = seminarRepository.save(seminarEntity).id
        userSeminarRepository.save(userSeminarEntity)
        return seminarRepositorySupport.getSeminarById(seminarId)
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
        val seminarId = seminarRepository.save(seminarEntity).id
        return seminarRepositorySupport.getSeminarById(seminarId)
    }

    @Transactional
    override fun getSeminarById(seminarId: Long): SeminarDto.SeminarProfileResponse {
        if (!seminarRepository.existsById(seminarId)) {
            throw Seminar404("This seminar doesn't exist.")
        }
        return seminarRepositorySupport.getSeminarById(seminarId)
    }

    @Transactional
    override fun getSeminars(
        name: String?,
        earliest: String?
    ): MutableList<SeminarDto.SeminarProfileSimplifiedResponse> {
        val doEarliest: Boolean = earliest.equals("earliest")
        return seminarRepositorySupport.getSeminars(name, doEarliest)
    }

    @Transactional
    override fun participateSeminar(seminarId: Long, role: String, userId: Long): SeminarDto.SeminarProfileResponse {
        if (!seminarRepository.existsById(seminarId)) {
            throw Seminar404("This seminar doesn't exist.")
        }
        if (role != "PARTICIPANT" && role != "INSTRUCTOR") {
            throw Seminar400("'role' should be either PARTICIPANT or INSTRUCTOR.")
        }
        val userEntity = userRepository.findById(userId).get()
        val seminarEntity = seminarRepository.findById(seminarId).get()
        for (userSeminarEntity in userEntity.userSeminarEntities) {
            if (userSeminarEntity.seminarEntity == seminarEntity) {
                if (userSeminarEntity.isActive)
                    throw Seminar400("You are already in this seminar as a " + userSeminarEntity.role + ".")
                else
                    throw Seminar400("You dropped this seminar before. You can not participate in this seminar again.")
            }
        }
        if (role == "PARTICIPANT") {
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
                if (userSeminarEntity.role == "INSTRUCTOR") {
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
        return seminarRepositorySupport.getSeminarById(seminarId)
    }

    @Transactional
    override fun dropSeminar(seminarId: Long, userId: Long): SeminarDto.SeminarProfileResponse {
        if (!seminarRepository.existsById(seminarId)) {
            throw Seminar404("This seminar doesn't exist.")
        }
        val userEntity = userRepository.findById(userId).get()
        if (userEntity.role == "INSTRUCTOR") {
            throw Seminar403("Instructor can not drop the seminar.")
        }
        val seminarEntity = seminarRepository.findById(seminarId).get()
        for (userSeminarEntity in userEntity.userSeminarEntities) {
            if (userSeminarEntity.seminarEntity == seminarEntity) {
                userSeminarEntity.isActive = false
                userSeminarEntity.droppedAt = LocalDateTime.now()
                userSeminarEntity.modifiedAt = LocalDateTime.now()
                seminarEntity.participantCount -= 1L
                userSeminarRepository.save(userSeminarEntity)
                break
            }
        }
        return seminarRepositorySupport.getSeminarById(seminarId)
    }

}