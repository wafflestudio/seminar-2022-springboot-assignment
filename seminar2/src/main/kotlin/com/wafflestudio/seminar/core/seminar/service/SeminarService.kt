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
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional

interface SeminarService {
    fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar
    fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar
    fun getSeminar(seminarId: Long): Seminar
    fun getAllSeminar(name: String?, order: String?, pageRequest: PageRequest): List<Seminar>
    fun addSeminar(userId: Long, seminarId: Long, roleRequest: RoleRequest): Seminar
    fun dropSeminar(userId: Long, seminarId: Long): User
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val seminarRepositorySupport: SeminarRepositorySupport,
    private val userSeminarRepositorySupport: UserSeminarRepositorySupport,
) : SeminarService {

    @Transactional
    override fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)!!

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
            joinedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            isActive = true,
        )

        seminar.users.add(userSeminar)
        seminarRepository.save(seminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarRequest.seminarId)
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
        if (seminar.hostId != userId) {
            throw Seminar403("세미나를 수정할 권한이 없습니다")
        }

        seminar.update(seminarRequest)
        return seminar.toSeminar()
    }

    @Transactional
    override fun getSeminar(seminarId: Long): Seminar {
        return seminarRepository.findByIdOrNull(seminarId)?.toSeminar()
            ?: throw Seminar404("존재하지 않는 seminarId 입니다")
    }

    @Transactional
    override fun getAllSeminar(name: String?, order: String?, pageRequest: PageRequest): List<Seminar> {
        return seminarRepositorySupport.findAll(name, order, pageRequest).map { it.toSeminar() }
    }

    @Transactional
    override fun addSeminar(userId: Long, seminarId: Long, roleRequest: RoleRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)!!
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
            joinedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            isActive = true,
        )
        seminar.users.add(userSeminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun dropSeminar(userId: Long, seminarId: Long): User {
        val user = userRepository.findByIdOrNull(userId)!!
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
        userSeminar?.droppedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        return user.toUser()
    }
}