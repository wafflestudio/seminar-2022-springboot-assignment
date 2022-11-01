package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.database.ParticipantEntity

data class ParticipantProfile(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    // TODO seminars
) {
    companion object {
        fun of(entity: ParticipantEntity) = entity.run {
            ParticipantProfile(
                id = id,
                university = university,
                isRegistered = isRegistered,
            )
        }
    }
}