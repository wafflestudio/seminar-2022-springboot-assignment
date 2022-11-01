package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository
) {
    fun getSeminar(id: Long): Seminar {
        val seminalEntity = seminarRepository.findByIdOrNull(id) ?: throw Seminar400("$id 의 세미나는 존재하지 않습니다.")
        val instructors = seminalEntity.userSeminars
            .filter { it.user.instructor != null }
            .map { it.toInstructor() }
            .toMutableList()
        val participants = seminalEntity.userSeminars
            .filter { it.user.participant != null }
            .map { it.toParticipant() }
            .toMutableList()

        return seminalEntity.toSeminar(instructors, participants)
    }
    
    @Transactional
    fun createSeminar(user: User, createSeminarDTO: CreateSeminarDTO): Seminar {
        val seminarEntity = seminarRepository.save(createSeminarDTO.toEntity())
        val userSeminarEntity = userSeminarRepository.save(UserSeminarEntity(
            user = user.toEntity(),
            seminar = seminarEntity,
            joinedAt = LocalDateTime.now(),
            isActive = false,
        ))
        seminarEntity.userSeminars.add(userSeminarEntity)
        val seminar = seminarEntity.toSeminar()
        seminar.instructors.add(userSeminarEntity.toInstructor())
        
        return seminar
    }
}