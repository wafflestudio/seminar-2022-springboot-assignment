package com.wafflestudio.seminar.core.user.domain

data class Participant(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: MutableSet<ParticipantSeminar>,
)