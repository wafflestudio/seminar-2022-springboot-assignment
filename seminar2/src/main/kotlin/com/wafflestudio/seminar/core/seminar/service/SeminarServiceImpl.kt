package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.*
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.domain.SeminarForList
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val authTokenService: AuthTokenService,
) : SeminarService {

    @LogExecutionTime
    override fun createSeminar(authToken: String, createSeminarRequest: CreateSeminarRequest): Seminar {
        val instructorId = authTokenService.getCurrentUserId(authToken)
        val instructor = userRepository.findByIdOrNull(instructorId)
        if (instructor!!.instructorProfileEntity == null) {
            throw Seminar403("세미나 진행자만 세미나를 생성할 수 있습니다.")
        }

        seminarRepository.findSeminarsByInstructorId(instructorId)?.let { throw Seminar400("이미 참여하고 있는 세미나가 존재합니다.") }

        if (createSeminarRequest.name == "" || createSeminarRequest.capacity!! <= 0 || createSeminarRequest.count!! <= 0 || createSeminarRequest.time == null) {
            throw Seminar400("필요한 정보를 모두 입력해주세요")
        }

        val regex = Regex("^[0-2][0-9]:[0-5][0-9]")
        if (!regex.matches(createSeminarRequest.time)) {
            Seminar400("올바른 시간 값을 입력해주세요")
        }
        val newSeminar = SeminarEntity(
            seminarName = createSeminarRequest.name!!,
            capacity = createSeminarRequest.capacity,
            count = createSeminarRequest.count,
            time = createSeminarRequest.time,
            online = createSeminarRequest.online!!
        )
        seminarRepository.save(newSeminar)
        val newUserSeminarRelation = UserSeminarEntity(user = instructor, isInstructor = true, seminar = newSeminar)
        userSeminarRepository.save(newUserSeminarRelation)
        return newSeminar.toDTO(
            mutableListOf(instructor.toInstructorDTO(newUserSeminarRelation.createdAt!!)),
            mutableListOf()
        )
    }

    @LogExecutionTime
    override fun modifySeminar(authToken: String, modifySeminarRequest: ModifySeminarRequest): Seminar {
        if (modifySeminarRequest.id == null) {
            throw Seminar400("변경할 세미나의 id를 입력해주세요.")
        }
        val userId = authTokenService.getCurrentUserId(authToken)
        val seminarId = modifySeminarRequest.id
        val seminar = seminarRepository.findByIdOrNull(seminarId)
        seminar ?: throw Seminar404("세미나를 찾을 수 없습니다.")
        val creatorSeminar = userSeminarRepository.findBySeminarId(seminarId)!!.sortedBy { it.createdAt }[0]
        if (creatorSeminar.user!!.id != userId) {
            throw Seminar403("세미나를 수정할 권한이 없습니다.")
        }
        modifySeminarRequest.name?.let {
            seminar.seminarName = it
        }
        modifySeminarRequest.capacity?.let {
            if (it > 0) {
                seminar.capacity = it
            } else {
                throw Seminar400("capacity에 0보다 큰 수를 입력해주세요.")
            }
        }
        modifySeminarRequest.count?.let {
            if (it > 0) {
                seminar.count = it
            } else {
                throw Seminar400("count에 0보다 큰 수를 입력해주세요.")
            }
        }
        modifySeminarRequest.time?.let {
            val regex = Regex("^[0-2][0-9]:[0-5][0-9]")
            if (!regex.matches(it)) {
                throw Seminar400("올바른 시간 값을 입력해주세요")
            } else {
                seminar.time = it
            }
        }
        modifySeminarRequest.online?.let {
            seminar.online = it
        }
        seminarRepository.save(seminar)

        val instructors = userSeminarRepository.findInstructorsById(seminarId)
        val participants = userSeminarRepository.findParticipantsById(seminarId)

        return seminar.toDTO(instructors, participants)
    }

    @LogExecutionTime
    override fun getAllSeminar(seminarName: String?, order: String?, pageable: Pageable): List<SeminarForList> {
        var seminars = if (seminarName != null) {
            seminarRepository.findSeminarByName(seminarName)
        } else {
            seminarRepository.findAll(pageable)
        }
        if (order == "earliest") {
            seminars = seminars.sortedByDescending { it.createdAt }
        }

        val seminarList = mutableListOf<SeminarForList>()
        seminars.forEach {
            val instructors = userSeminarRepository.findInstructorsById(it.id)
            val participantCount = userSeminarRepository.findActiveParticipantCountById(it.id)
            seminarList.add(it.toListDTO(instructors, participantCount))
        }
        return seminarList
    }

    @LogExecutionTime
    override fun readSeminar(seminarId: Long): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId)
        seminar ?: throw Seminar404("존재하지 않는 세미나입니다.")

        val instructors = userSeminarRepository.findInstructorsById(seminarId)
        val participants = userSeminarRepository.findParticipantsById(seminarId)

        return seminar.toDTO(instructors, participants)
    }

    @LogExecutionTime
    override fun applySeminar(authToken: String, seminarId: Long, applySeminarRequest: ApplySeminarRequest): Seminar {
        val userId = authTokenService.getCurrentUserId(authToken)
        val user = userRepository.findByIdOrNull(userId)
        if (applySeminarRequest.role == null || (applySeminarRequest.role.lowercase() != "instructor" && applySeminarRequest.role.lowercase() != "participant")) {
            throw Seminar400("올바른 role을 입력해주세요.")
        }
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404("세미나를 찾을 수 없습니다.")
        val existingUserSeminar = userSeminarRepository.findUserSeminarBySeminarIdAndUserId(seminarId, userId)
        if (existingUserSeminar != null) {
            if (existingUserSeminar.isActive == false) {
                throw Seminar400("중도포기한 세미나는 다시 참여할 수 없습니다.")
            }
            throw Seminar400("이미 세미나에 참여하고 있습니다.")
        }
        if (applySeminarRequest.role == "participant") {
            if (user!!.participantProfileEntity == null) {
                throw Seminar403("세미나 참여 자격이 없습니다.")
            } else {
                if (user.participantProfileEntity!!.isRegistered == false) {
                    throw Seminar403("활성회원이 아닙니다.")
                }
                val participantCount = userSeminarRepository.findActiveParticipantCountById(seminarId)
                if (participantCount == seminar.capacity.toLong()) {
                    throw Seminar400("이미 수강 정원이 가득찼습니다.")
                }
                val newUserSeminarRelation = UserSeminarEntity(user = user, isInstructor = false, seminar = seminar)
                userSeminarRepository.save(newUserSeminarRelation)
                
            }
        } else if (applySeminarRequest.role == "instructor") {
            if (user!!.instructorProfileEntity == null) {
                throw Seminar403("세미나 진행 자격이 없습니다.")
            }
            if (userSeminarRepository.findAllSeminarByInstructorId(userId).isNotEmpty()) {
                throw Seminar400("이미 진행하는 세미나가 존재합니다.")
            }
            val newUserSeminarRelation = UserSeminarEntity(user = user, isInstructor = true, seminar = seminar)
            userSeminarRepository.save(newUserSeminarRelation)
        }
        val instructors = userSeminarRepository.findInstructorsById(seminarId)
        val participants = userSeminarRepository.findParticipantsById(seminarId)

        return seminar.toDTO(instructors, participants)
    }

    @LogExecutionTime
    override fun deleteParticipantFromSeminar(authToken: String, seminarId: Long): String {
        val userId = authTokenService.getCurrentUserId(authToken)
        val user = userRepository.findByIdOrNull(userId) ?: throw Seminar400("올바른 유저 Id가 아닙니다")
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404("존재하지 않는 세미나입니다")
        val userSeminar = userSeminarRepository.findUserSeminarBySeminarIdAndUserId(seminarId, userId)
        if (userSeminar != null) {
            if (userSeminar.isInstructor) {
                throw Seminar403("세미나 진행자는 세미나를 드랍할 수 없습니다.")
            }
            if (userSeminar.isActive == false) {
                throw Seminar403("이미 드랍한 세미나입니다.")
            }
            userSeminar.modifiedAt = LocalDateTime.now()
            userSeminar.isActive = false
            userSeminarRepository.save(userSeminar)
            return "세미나를 드랍하였습니다"
        }
        return ""
    }
}