package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.dto.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.dto.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.SeminarInfo
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.UserNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface SeminarService {
    fun createSeminar(user_id: Long, createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse
    fun getSeminarOption(name: String?, order: String?): List<SeminarInfo>
    fun getSeminarById(seminar_id: Long): SeminarInfo
    fun participateSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse
    fun instructSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse
    fun dropSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val participantSeminarTableRepository: ParticipantSeminarTableRepository,
    private val instructorSeminarTableRepository: InstructorSeminarTableRepository,
): SeminarService {

    @Transactional
    override fun createSeminar(user_id: Long, createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse {
        val seminarEntity = seminarRepository.save(
            createSeminarRequest.toSeminarEntity()
        )
        
        val instructorSeminarTableEntity = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                userRepository.findByIdOrNull(user_id) ?: throw UserNotFoundException,
                seminarEntity,
            )
        )
        
        seminarEntity.instructorSet.add(instructorSeminarTableEntity)
        
        return seminarEntity.toCreateSeminarResponse()
    }

    override fun getSeminarOption(name: String?, order: String?): List<SeminarInfo> {
        val seminarEntityList = seminarRepository.findByNameLatest(name)
        
        if (order == "earliest") {
            seminarEntityList.reverse()
        }
        
        val seminarInfoList = mutableListOf<SeminarInfo>()
        seminarEntityList.forEach {
            seminarInfoList.add(it.toSeminarInfo())
        }
        
        return seminarInfoList
    }

    override fun getSeminarById(seminar_id: Long): SeminarInfo {
        return seminarRepository.findByIdOrNull(seminar_id)
            ?.toSeminarInfo()
            ?: throw SeminarNotFoundException
    }

    @Transactional
    override fun participateSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw UserNotFoundException
        if (user.participantProfile == null || !user.participantProfile!!.isRegistered) {
            throw NotAllowedToParticipateException
        }
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw SeminarNotFoundException
        if (seminar.capacity <= seminar.participantSet.size) {
            throw SeminarCapacityFullException
        }
        user.participatingSeminars.forEach {
            if (it.seminar == seminar) {
                when (it.isActive) {
                    true -> throw AlreadyParticipatingException
                    false -> throw DroppedSeminarException
                }
            }
        }
        
        val participantSeminarTableEntity = participantSeminarTableRepository.save(
            ParticipantSeminarTableEntity(
                user,
                seminar,
                true,
                null
            )
        )
        
        seminar.participantSet.add(participantSeminarTableEntity)
        
        return seminar.toCreateSeminarResponse()
    }

    @Transactional
    override fun instructSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw UserNotFoundException
        if (user.instructorProfile == null) {
            throw NotAllowedToInstructException
        }
        if (user.instructingSeminars.size > 0) {
            throw MultipleInstructingSeminarException
        }
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw SeminarNotFoundException

        val instructorSeminarTableEntity = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                user,
                seminar,
            )
        )

        seminar.instructorSet.add(instructorSeminarTableEntity)

        return seminar.toCreateSeminarResponse()
    }

    @Transactional
    override fun dropSeminar(user_id: Long, seminar_id: Long): CreateSeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw UserNotFoundException
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw SeminarNotFoundException
        user.instructingSeminars.forEach { 
            if (it.seminar == seminar) {
                throw InstructorNotAllowedToDropException
            }
        }
        
        user.participatingSeminars.forEach { 
            if (it.seminar == seminar) {
                it.isActive = false
                it.droppedAt = LocalDateTime.now()
            }
        }
        
        return seminar.toCreateSeminarResponse()
    }
}
