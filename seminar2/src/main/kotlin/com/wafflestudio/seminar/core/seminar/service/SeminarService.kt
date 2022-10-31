package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface SeminarService {
    fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar
    fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar
    fun getSeminar(userId: Long, seminarId: Long): Seminar
    fun getAllSeminar(userId: Long): List<Seminar>
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
) : SeminarService {

    @Transactional
    override fun makeSeminar(userId: Long, seminarRequest: SeminarRequest): Seminar {
        val user = userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")

        if (seminarRepository.findByHostId(userId) != null)
            throw Seminar409("이미 세미나를 진행하고 있어요")

        user.instructor ?: throw Seminar409("세미나 진행자가 아닙니다")

        val seminar = SeminarEntity(
            name = seminarRequest.name,
            capacity = seminarRequest.capacity,
            count = seminarRequest.count,
            time = seminarRequest.time,
            online = seminarRequest.online,
            hostId = userId,
        )

        seminarRepository.save(seminar)
        return seminar.toSeminar()
    }

    @Transactional
    override fun editSeminar(userId: Long, seminarRequest: EditSeminarRequest): Seminar {
        userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")

        val hostId = seminarRequest.seminarId
        val seminar = seminarRepository.findByHostId(userId)
        if (seminar?.hostId != hostId) {
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
    override fun getAllSeminar(userId: Long): List<Seminar> {
        userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw Seminar404("존재하지 않는 userId 입니다")
        return seminarRepository.findAll().map { it.toSeminar() }
    }
}