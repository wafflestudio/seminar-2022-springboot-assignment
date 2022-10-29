package com.wafflestudio.seminar.core.user.domain

data class ParticipantProfile(
    val user: User,
    val university: String,
    val isRegistered: Boolean
) 