package com.wafflestudio.seminar.core.profile.dto

import com.wafflestudio.seminar.core.seminar.dto.ParticipantSeminarResponse

data class ParticipantProfileResponse(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: List<ParticipantSeminarResponse> = listOf(),
)