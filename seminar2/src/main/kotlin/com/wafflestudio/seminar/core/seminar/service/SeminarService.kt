package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.GetSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarDslRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarDslRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeminarService(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val seminarDslRepository: SeminarDslRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userSeminarDslRepository: UserSeminarDslRepository,
) {
    @Transactional
    fun createSeminar(userId: Long, request: CreateSeminarRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)!!
        val seminar = seminarRepository.save(request.toEntity(user))
        return seminar.toDto()
    }

    fun getSeminar(seminarId: Long): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404()
        return seminar.toDto()
    }

    private fun SeminarEntity.toDto(): Seminar {
        val userSeminars = userSeminarRepository.findAllBySeminarId(id)
        val (instructors, participants) = userSeminars.partition { us -> us.isInstructor }

        val instructorProjections = userSeminarDslRepository.findInstructorProjections(instructors.map { it.id })
        val participantProjections = userSeminarDslRepository.findParticipantProjections(participants.map { it.id })

        return Seminar.of(this, instructorProjections, participantProjections)
    }

    // TODO 다양한 방식에 따른 쿼리 수를 설명할 수 있어야 한다.
    fun getSeminarList(request: GetSeminarRequest): List<Seminar> {
        val (name, order) = request
        val isAscending = order == "earliest"
        // seminar + userSeminar fetch join
        val seminars = seminarDslRepository.getList(name, isAscending)

        // userSeminar의 user를 한번에 모두 가져와서 딕셔너리에 담아둠
        val userIds = seminars.flatMap { it.userSeminars.map { it.userId } }
        val userMap = userRepository.findAllById(userIds).associateBy { it.id }

        // 조합해서 반환
        return seminars.map { seminar ->
            val userSeminars = seminar.userSeminars
            val (instructors, participants) = userSeminars.partition { it.isInstructor }
            val instProjections = instructors.map { inst -> SeminarInstructor.of(userMap[inst.userId]!!, inst) }
            val partProjections = participants.map { part -> SeminarParticipant.of(userMap[part.userId]!!, part) }
            Seminar.of(seminar, instProjections, partProjections)
        }
    }

    @Transactional
    fun joinToSeminar(userId: Long, seminarId: Long, role: UserSeminarEntity.Role): Seminar {
        val user = userRepository.findByIdOrNull(userId)!!
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404()
        checkUserIsInstructorOfAnother(userId, role)
        seminar.addUser(user, role)
        return seminar.toDto()
    }

    @Transactional
    fun dropSeminar(userId: Long, seminarId: Long): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw Seminar404()
        val userSeminars = userSeminarRepository.findAllBySeminarId(seminarId)
        val userSeminar = userSeminars.find { it.userId == userId }
        userSeminar?.drop()

        val (instructors, participants) = userSeminars.partition { us -> us.isInstructor }
        val instructorProjections = userSeminarDslRepository.findInstructorProjections(instructors.map { it.id })
        val participantProjections = userSeminarDslRepository.findParticipantProjections(participants.map { it.id })
        return Seminar.of(seminar, instructorProjections, participantProjections)
    }

    private fun checkUserIsInstructorOfAnother(userId: Long, role: UserSeminarEntity.Role) {
        if (role != UserSeminarEntity.Role.INSTRUCTOR) {
            return
        }

        val userInstructingSeminars = userSeminarRepository.findAllByUserId(userId).filter { it.isInstructor }
        require(userInstructingSeminars.isEmpty()) {
            throw Seminar400("한 명의 강사가 동시에 여러 강좌를 담당할 수 없습니다.")
        }
    }

    @Transactional
    fun updateSeminar(userId: Long, request: UpdateSeminarRequest) {
        val user = userRepository.findByIdOrNull(userId)!!
        val seminar = seminarRepository.findByIdOrNull(request.id) ?: throw Seminar404()

        seminar.update(user, request)
    }
}