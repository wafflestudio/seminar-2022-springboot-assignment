package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.CreateParticipantDTO
import com.wafflestudio.seminar.core.user.database.ParticipantEntity
import com.wafflestudio.seminar.core.user.database.ParticipantRepository
import org.springframework.stereotype.Service

@Service
class ParticipantService(
    private val participantRepository: ParticipantRepository,
) {
    fun getParticipant(id: Long) {
        participantRepository.findById(id)
    }
    
    fun createParticipant(createParticipantDTO: CreateParticipantDTO): ParticipantEntity {
        val newParticipant = ParticipantEntity(
            university = createParticipantDTO.university,
            isRegistered = true,
        )
        return participantRepository.save(newParticipant)
    }
}