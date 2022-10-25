package com.wafflestudio.seminar.core.user.dto

data class ParticipantProfileDto(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: SeminarDto
) {
}