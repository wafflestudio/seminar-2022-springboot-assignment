package com.wafflestudio.seminar.core.user.dto.user

import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto

data class GetProfileParticipantDto(
    val id: Long?,
    val university: String?,
    val isRegistered: Boolean?,
    // val seminars: List<SeminarsDto>?
    
) {
    companion object {
        fun of(participantProfileEntity: ParticipantProfileEntity) : GetProfileParticipantDto{
            participantProfileEntity.run { 
                return GetProfileParticipantDto(
                        id = id,
                        university = university,
                        isRegistered = isRegistered
                )
            }
        }
    }
}