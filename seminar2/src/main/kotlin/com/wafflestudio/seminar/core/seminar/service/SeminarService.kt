package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.SeminarDetailInfo
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
        val user = findInstructor(user_id)
        if (user.instructingSeminars.size > 0) {
            throw MultipleInstructingSeminarException
        }

        val seminarEntity = seminarRepository.save(
            createSeminarRequest.toSeminarEntity()
        )

        val instructorSeminarTableEntity = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                user,
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
        val instructorSeminarTableEntity = instructorSeminarTableRepository.findByInstructorId(user_id)
            ?: throw NoInstructingSeminarException
        val seminar = instructorSeminarTableEntity.seminar
        if (seminar.id != updateSeminarRequest.id) {
            throw NotAllowedToUpdateSeminarException
        }

        return seminar.updateSeminar(updateSeminarRequest)
    }

    @Transactional
    override fun getSeminarOption(name: String?, order: String?): List<SeminarInfo> {
        val seminarEntityList = name
            ?.let { seminarRepository.findByNameLatest(name) }
            ?: seminarRepository.findAll()

        if (order == "earliest") {
            seminarEntityList.reverse()
        }

        val seminarInfoList = mutableListOf<SeminarInfo>()
        seminarEntityList.forEach {
            seminarInfoList.add(it.toSeminarInfo())
        }

        return seminarInfoList
    }

    @Transactional
    override fun getSeminarById(seminar_id: Long): SeminarInfo {
        return findSeminar(seminar_id).toSeminarInfo()
    }

    @Transactional
    override fun participateSeminar(user_id: Long, seminar_id: Long): SeminarDetailInfo {
        val user = userRepository.findUserWithAllInfo(user_id)
            ?: throw UserNotFoundException
        if (user.participantProfile == null || !user.participantProfile!!.isRegistered) {
            throw NotAllowedToParticipateException
        }
        val seminar = seminarRepository.findWithParticipants(seminar_id)
            ?: throw SeminarNotFoundException
        if (seminar.capacity <= seminar.participantSet.size) {
            throw SeminarCapacityFullException
        }
        user.participatingSeminars.find {
            it.seminar == seminar
        }?.let {
            when (it.isActive) {
                true -> throw AlreadyParticipatingException
                false -> throw DroppedSeminarException
            }
        }
        if (user.instructingSeminars.isNotEmpty() && user.instructingSeminars.first() == seminar) {
            throw CannotParticipateInstructingSeminarException
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
        val user = findInstructor(user_id)
        if (user.instructorProfile == null) {
            throw NotAllowedToInstructException
        }
        if (user.instructingSeminars.size > 0) {
            throw MultipleInstructingSeminarException
        }
        val seminar = seminarRepository.findWithInstructors(seminar_id)
            ?: throw SeminarNotFoundException

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
        val user = findUserWithAllInfo(user_id)
        val seminar = findSeminar(seminar_id)
        user.instructingSeminars.find {
            it.seminar == seminar
        }?.let {
            throw InstructorNotAllowedToDropException
        }

        user.participatingSeminars.find {
            it.seminar == seminar
        }?.let {
            it.isActive = false
            it.droppedAt = LocalDateTime.now()
        }

        return seminar.toSeminarDetailInfo()
    }

    private fun findInstructor(user_id: Long): UserEntity = userRepository.findInstructor(user_id)
        ?: throw UserNotFoundException

    private fun findUserWithAllInfo(user_id: Long): UserEntity = userRepository.findUserWithAllInfo(user_id)
        ?: throw UserNotFoundException

    private fun findSeminar(seminar_id: Long): SeminarEntity = seminarRepository.findByIdOrNull(seminar_id)
        ?: throw SeminarNotFoundException
}