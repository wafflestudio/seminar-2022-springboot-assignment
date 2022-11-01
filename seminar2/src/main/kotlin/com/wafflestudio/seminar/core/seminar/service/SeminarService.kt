package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeminarService(
    private val seminarRepository: SeminarRepository,
) {
    fun getSeminar(id: Long) = seminarRepository.findById(id)
    
    @Transactional
    fun createSeminar(user: User, createSeminarDTO: CreateSeminarDTO): SeminarEntity { 
        return seminarRepository.save(createSeminarDTO.toEntity())
    }
}