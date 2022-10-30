package com.wafflestudio.seminar.core.user.dto.user

import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto

data class GetProfileParticipantDto(
    val id: Long?,
    val university: String?,
    val isRegistered: Boolean?,
    val seminars: List<SeminarsDto>?
    
) {
    
}