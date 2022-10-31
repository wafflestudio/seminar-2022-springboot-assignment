package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.RoleRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

interface SeminarService {
    fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar
    fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar
    fun getSeminar(userId: Long, seminarId: Long): Seminar
    fun getAllSeminar(userId: Long, name: String, order: String): List<Seminar>

    fun addSeminar(userId: Long, seminarId: Long, roleRequest: RoleRequest): Seminar
    fun dropSeminar(userId: Long, seminarId: Long): User
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userSeminarRepositorySupport: UserSeminarRepositorySupport,
) : SeminarService {

    @Transactional
    override fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")

        if (seminarRepository.findByHostId(userId) != null)
            throw Seminar400("이미 세미나를 진행하고 있어요")

        user.instructor ?: throw Seminar409("세미나 진행자가 아닙니다")

        val seminar = SeminarEntity(
            name = seminarRequest.name,
            capacity = seminarRequest.capacity,
            count = seminarRequest.count,
            time = seminarRequest.time,
            online = seminarRequest.online,
            hostId = userId,
        )

        val userSeminar = UserSeminarEntity(
            user = user,
            seminar = seminar,
            role = User.Role.INSTRUCTOR,
            joinedAt = LocalDateTime.now(),
            isActive = true,
        )

        seminar.users.add(userSeminar)
        userSeminarRepository.save(userSeminar)
        seminarRepository.save(seminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar {
        userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")

        val seminar = seminarRepository.findByIdOrNull(seminarRequest.seminarId)
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
        if (seminar.hostId != userId) {
            throw Seminar403("세미나를 수정할 권한이 없습니다")
        }

        seminar.update(seminarRequest)
        seminarRepository.save(seminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun getSeminar(userId: Long, seminarId: Long): Seminar {
        userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw Seminar404("존재하지 않는 userId 입니다")
        return seminarRepository.findByIdOrNull(seminarId)?.toSeminar()
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
    }

    @Transactional
    override fun getAllSeminar(userId: Long, name: String, order: String): List<Seminar> {
        userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw Seminar404("존재하지 않는 userId 입니다")

        var seminars = seminarRepository.findAll()
            .filter { it.name.contains(name) }
            .sortedBy { it.createdAt }
            .map { it.toSeminar() }
            .reversed()
        
        if (order == "earliest") {
            seminars = seminars.reversed()
        }

        return seminars
    }

    @Transactional
    override fun addSeminar(userId: Long, seminarId: Long, roleRequest: RoleRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw Seminar404("존재하지 않는 userId 입니다")
        val seminar = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
        val existUserSeminar = userSeminarRepositorySupport
            .find(userId = userId, seminarId = seminarId)

        if (existUserSeminar != null) {
            if (existUserSeminar.isActive) {
                throw Seminar400("이미 참여 중인 세미나입니다")
            } else throw Seminar400("드랍한 세미나에 다시 참여할 수 없습니다")
        }

        if (roleRequest.role == User.Role.INSTRUCTOR) {
            user.instructor ?: throw Seminar403("진행 자격이 없습니다")
            if (seminarRepository.findByHostId(userId) != null) {
                throw Seminar400("이미 세미나를 진행하고 있습니다")
            }
        }

        if (roleRequest.role == User.Role.PARTICIPANT) {
            user.participant ?: throw Seminar403("참여 자격이 없습니다")
            if (!user.participant!!.isRegistered) {
                throw Seminar403("활성회원이 아닙니다")
            }
        }

        if (seminar.capacity <= seminar.toSeminar().participantCount) {
            throw Seminar400("세미나 정원이 가득 찼습니다")
        }

        val userSeminar = UserSeminarEntity(
            user = user,
            seminar = seminar,
            role = roleRequest.role,
            joinedAt = LocalDateTime.now(),
            isActive = true,
        )
        seminar.users.add(userSeminar)
        userSeminarRepository.save(userSeminar)
        seminarRepository.save(seminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun dropSeminar(userId: Long, seminarId: Long): User {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw Seminar404("존재하지 않는 userId 입니다")
        seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
        val userSeminar = userSeminarRepositorySupport
            .find(userId = userId, seminarId = seminarId)

        if (userSeminar?.role == User.Role.INSTRUCTOR) {
            throw Seminar403("세미나 진행자는 드랍할 수 없습니다")
        }
        if (userSeminar?.isActive == false) {
            throw Seminar403("이미 드랍한 세미나입니다")
        }
        userSeminar?.isActive = false
        userSeminar?.droppedAt = LocalDateTime.now()
        userSeminar?.let {
            userSeminarRepository.save(userSeminar)
        }
        return user.toUser()
    }
}