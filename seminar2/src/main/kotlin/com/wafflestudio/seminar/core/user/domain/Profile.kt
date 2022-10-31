package com.wafflestudio.seminar.core.user.domain

data class ParticipantProfile(
    val id: Long,
    val university: String?,
    val isRegistered: Boolean,
    // TODO: Seminar List
)

data class InstructorProfile(
    val id: Long,
    val company: String?,
    val year: Int?,
    // TODO: Seminar List
)