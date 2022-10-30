package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarMakeRequest
import com.wafflestudio.seminar.core.seminar.api.response.CountSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.repository.Seminar
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.seminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.user.Role
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.exception.Seminar400
import com.wafflestudio.seminar.exception.Seminar403
import com.wafflestudio.seminar.exception.Seminar404
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.streams.toList

private const val INSTRUCTOR = "Instructor"
private const val PARTICIPANT = "Participant"

@Service
@Transactional
class SeminarService(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
)
{
    fun makeSeminar(user: UserEntity, request: SeminarMakeRequest): SeminarResponse {
        checkInstructor(user)
        checkHasAlreadyInstructSeminar(user)
        val seminar = user.openSeminar(Seminar(request))
        val savedSeminar = seminarRepository.save(seminar)
        return SeminarResponse.of(savedSeminar)
    }

    fun editSeminar(user: UserEntity, request: EditSeminarRequest): SeminarResponse {
        val userSeminar = findInstructUserSeminar(user)
            .orElseThrow { Seminar403("진행하는 세미나가 존재하지 않습니다.") }
        val seminar = userSeminar.seminar
        seminar.name = request.name
        seminar.capacity = request.capacity
        seminar.count = request.count
        seminar.online = request.online
        return SeminarResponse.of(seminar)
    }

    fun findSeminarById(seminarId: Long): SeminarResponse {
        val seminar = seminarRepository.findById(seminarId)
            .orElseThrow { Seminar404("ID: $seminarId 인 세미나가 존재하지 않습니다.") }
        return SeminarResponse.of(seminar)
    }

    fun findSeminarByQuery(name: String, order: String): List<CountSeminarResponse> {
        if (order == "earliest") {
            return seminarRepository.findByNameContainingOrderByCreatedAtAsc(name)
                .stream()
                .map(CountSeminarResponse.Companion::of)
                .toList()
        }

        return seminarRepository.findByNameContainingOrderByCreatedAtDesc(name)
            .stream()
            .map(CountSeminarResponse.Companion::of)
            .toList()
    }

    fun joinSeminar(user: UserEntity, role: String, seminarId: Long): SeminarResponse {
        val findUser = userRepository.findById(user.id).get()
        val seminar = seminarRepository.findById(seminarId)
            .orElseThrow { Seminar404("ID: $seminarId 인 세미나가 존재하지 않습니다.") }

        return when (role) {
            INSTRUCTOR -> {
                val instructSeminar = instructSeminar(findUser, seminar)
                SeminarResponse.of(instructSeminar)
            }
            PARTICIPANT -> {
                val participateSeminar = participateSeminar(findUser, seminar)
                SeminarResponse.of(participateSeminar)
            }
            else -> {
                throw Seminar400("잘못된 role입니다.")
            }
        }
    }

    fun dropSeminar(user: UserEntity, seminarId: Long): SeminarResponse {
        val findUser = userRepository.findById(user.id).get()
        val seminar = seminarRepository.findById(seminarId)
            .orElseThrow { Seminar404("ID: $seminarId 인 해당하는 세미나가 존재하지 않습니다.") }
        val _userSeminar = userSeminarRepository.findByUserEntityAndSeminar(findUser, seminar)
        
        if (_userSeminar.isEmpty)
            return SeminarResponse.of(seminar)
        
        val userSeminar = _userSeminar.get()
        if (userSeminar.role == Role.Instructor)
            throw Seminar403("세미나 진행자는 세미나 드랍을 할 수 없습니다.")
        
        if (!userSeminar.isActive)
            throw Seminar403("이미 드랍한 세미나입니다.")
        
        userSeminar.dropSeminar()
        return SeminarResponse.of(seminar)
    }

    private fun instructSeminar(user: UserEntity, seminar: Seminar): Seminar {
        checkInstructor(user)
        checkJoinedSeminar(user, seminar)
        checkAlreadyInstructSeminar(user)
        user.instructSeminar(seminar)
        return seminar
    }

    private fun checkAlreadyInstructSeminar(user: UserEntity) {
        if (hasInstructSeminar(user))
            throw Seminar400("이미 담당하고 있는 세미나가 존재합니다.")
    }

    private fun hasInstructSeminar(user: UserEntity): Boolean {
        return userSeminarRepository.findByUserEntity(user)
            .stream()
            .anyMatch { e -> e.role == Role.Instructor }
    }

    private fun participateSeminar(user: UserEntity, seminar: Seminar): Seminar {
        checkParticipant(user)
        checkDropped(user, seminar)
        checkSeminarIsFull(seminar)
        checkJoinedSeminar(user, seminar)
        user.participateSeminar(seminar)
        return seminar
    }

    private fun checkDropped(user: UserEntity, seminar: Seminar) {
        if (hasBeenDropped(user, seminar))
            throw Seminar400("드랍했던 세미나는 참여할 수 없습니다.")
    }

    private fun hasBeenDropped(user: UserEntity, seminar: Seminar): Boolean {
        return user.userSeminars
            .stream()
            .anyMatch { it.seminar == seminar && it.droppedAt != null }
    }

    private fun checkJoinedSeminar(user: UserEntity, seminar: Seminar) {
        if (alreadyJoined(user, seminar))
            throw Seminar400("이미 참여중인 세미나입니다.")
    }

    private fun alreadyJoined(user: UserEntity, seminar: Seminar): Boolean {
        return userSeminarRepository.findByUserEntity(user)
            .stream()
            .anyMatch { e -> e.seminar == seminar }
    }

    private fun checkSeminarIsFull(seminar: Seminar) {
        if (seminar.capacity - seminar.participantCount <= 0)
            throw Seminar400("세미나 정원이 가득 찼습니다.")
    }

    private fun findInstructUserSeminar(user: UserEntity): Optional<UserSeminar> {
        return userSeminarRepository.findByUserEntity(user)
            .stream()
            .filter { it.role == Role.Instructor }
            .findAny()
    }

    private fun checkHasAlreadyInstructSeminar(user: UserEntity) {
        findInstructUserSeminar(user)
            .ifPresent { throw Seminar403("이미 담당하는 세미나가 존재합니다.") }
    }

    private fun checkInstructor(user: UserEntity) {
        if (user.instructor == null)
            throw Seminar403("진행자 자격이 없습니다.")
    }

    private fun checkParticipant(user: UserEntity) {
        if (user.participant == null)
            throw Seminar403("수강생 자격이 없습니다.")
        if (!user.participant!!.isRegistered)
            throw Seminar403("활성회원이 아닙니다.")
    }
    
}
    