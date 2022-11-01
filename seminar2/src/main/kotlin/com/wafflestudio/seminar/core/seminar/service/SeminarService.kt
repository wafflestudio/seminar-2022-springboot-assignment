package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserEntity
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
        val seminarEntity = seminarRepository.findByIdOrNull(id) ?: throw Seminar400("$id 의 세미나는 존재하지 않습니다.")
        return seminarEntity.toSeminar()
    }
    
    fun getAllSeminar(): List<Seminar> {
        return seminarRepository.findAll().map { it.toSeminar() }
    }
    
    @Transactional
    fun createSeminar(user: UserEntity, createSeminarDTO: CreateSeminarDTO): Seminar {
        val seminarEntity = seminarRepository.save(createSeminarDTO.toEntity())
        val userSeminarEntity = userSeminarRepository.save(UserSeminarEntity(
            user = user,
            seminar = seminarEntity,
            joinedAt = LocalDateTime.now(),
            isActive = false,
        ))
        user.userSeminars.add(userSeminarEntity)
        seminarEntity.userSeminars.add(userSeminarEntity)
        
        return seminarEntity.toSeminar()
    }
}