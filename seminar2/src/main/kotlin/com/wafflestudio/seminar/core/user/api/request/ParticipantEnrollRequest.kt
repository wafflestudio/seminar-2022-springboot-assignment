package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity

data class ParticipantEnrollRequest(
    val university: String? = null,
    val isRegistered: Boolean = true,
) {

    fun toParticipantProfileEntity(): ParticipantProfileEntity = ParticipantProfileEntity(
        university,
        isRegistered,
    )
}