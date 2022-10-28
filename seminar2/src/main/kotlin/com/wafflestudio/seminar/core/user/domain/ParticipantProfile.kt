package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.SeminarForParticipantProfile

data class ParticipantProfile(
    val id: Long,
    val university: String? = "",
    val isRegistered: Boolean? = true,
    val seminars: List<SeminarForParticipantProfile>? = null
)
