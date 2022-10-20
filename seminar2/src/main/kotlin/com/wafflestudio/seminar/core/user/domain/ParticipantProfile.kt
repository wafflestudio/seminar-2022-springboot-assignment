package com.wafflestudio.seminar.core.user.domain

data class ParticipantProfile(
    val id: Long,
    val university: String = "",
    val isRegistered: Boolean = true,
    val seminars: List<UserParticipantSeminar> = ArrayList()
) {
}