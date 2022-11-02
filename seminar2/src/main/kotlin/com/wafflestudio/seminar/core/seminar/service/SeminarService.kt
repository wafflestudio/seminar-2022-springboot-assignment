package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarDetailInfo
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.SeminarInfo
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.UserNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface SeminarService {
    fun createSeminar(user_id: Long, createSeminarRequest: CreateSeminarRequest): SeminarDetailInfo
    fun updateSeminar(user_id: Long, updateSeminarRequest: UpdateSeminarRequest): SeminarDetailInfo
    fun getSeminarOption(name: String?, order: String?): List<SeminarInfo>
    fun getSeminarById(seminar_id: Long): SeminarInfo
    fun participateSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo
    fun instructSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo
    fun dropSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val participantSeminarTableRepository: ParticipantSeminarTableRepository,
    private val instructorSeminarTableRepository: InstructorSeminarTableRepository,
) : SeminarService {

    @Transactional
    override fun createSeminar(user_id: Long, createSeminarRequest: CreateSeminarRequest): SeminarDetailInfo {
        val seminarEntity = seminarRepository.save(
            createSeminarRequest.toSeminarEntity()
        )

        val instructorSeminarTableEntity = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                findUser(user_id),
                seminarEntity,
            )
        )

        seminarEntity.instructorSet.add(instructorSeminarTableEntity)

        return seminarEntity.toSeminarDetailInfo()
    }

    @Transactional
    override fun updateSeminar(user_id: Long, updateSeminarRequest: UpdateSeminarRequest): SeminarDetailInfo {
        if (updateSeminarRequest.name != null && updateSeminarRequest.name.isEmpty()) {
            throw BlankSeminarNameNotAllowedException
        }
        val seminar = findSeminar(updateSeminarRequest.id)
        val instructorSeminarTableEntity = instructorSeminarTableRepository.findByInstructorId(user_id)
            ?: throw NoInstructingSeminarException
        if (instructorSeminarTableEntity.seminar != seminar) {
            throw NotAllowedToUpdateSeminarException
        }
        
        seminar.let { 
            it.name = updateSeminarRequest.name ?: it.name
            it.capacity = updateSeminarRequest.capacity ?: it.capacity
            it.count = updateSeminarRequest.count ?: it.count
            it.time = updateSeminarRequest.time ?: it.time
            it.online = updateSeminarRequest.online ?: it.online
        }
        
        return seminar.toSeminarDetailInfo()
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
        return findSeminar(seminar_id).toSeminarInfo()
    }

    @Transactional
    override fun participateSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo {
        val user = findUser(user_id)
        if (user.participantProfile == null || !user.participantProfile!!.isRegistered) {
            throw NotAllowedToParticipateException
        }
        val seminar = findSeminar(seminar_id)
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

        return seminar.toSeminarDetailInfo()
    }

    @Transactional
    override fun instructSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo {
        val user = findUser(user_id)
        if (user.instructorProfile == null) {
            throw NotAllowedToInstructException
        }
        if (user.instructingSeminars.size > 0) {
            throw MultipleInstructingSeminarException
        }
        val seminar = findSeminar(seminar_id)

        val instructorSeminarTableEntity = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                user,
                seminar,
            )
        )

        seminar.instructorSet.add(instructorSeminarTableEntity)

        return seminar.toSeminarDetailInfo()
    }

    @Transactional
    override fun dropSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo {
        val user = findUser(user_id)
        val seminar = findSeminar(seminar_id)
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

        return seminar.toSeminarDetailInfo()
    }

    private fun findUser(user_id: Long): UserEntity = userRepository.findByIdOrNull(user_id)
        ?: throw UserNotFoundException
    
    private fun findSeminar(seminar_id: Long): SeminarEntity = seminarRepository.findByIdOrNull(seminar_id)
        ?: throw SeminarNotFoundException
}
