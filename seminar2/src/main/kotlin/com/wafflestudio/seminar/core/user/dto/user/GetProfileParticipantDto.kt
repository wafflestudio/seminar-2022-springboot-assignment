package com.wafflestudio.seminar.core.user.dto.user

import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity


data class GetProfileParticipantDto(
    val id: Long?,
    val university: String?,
    val isRegistered: Boolean?,
    val seminars: List<SeminarsDto>?
    
) {
    companion object {
        fun of(participantProfileEntity: ParticipantProfileEntity, seminarsDtoList: List<SeminarsDto>?) : GetProfileParticipantDto{
            participantProfileEntity.run { 
                return GetProfileParticipantDto(
                        id = id,
                        university = university,
                        isRegistered = isRegistered,
                        seminars = seminarsDtoList
                )
            }
        }
    }
}