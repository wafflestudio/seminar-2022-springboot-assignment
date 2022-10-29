package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.User

data class ParticipantProfile(
    val user: User,
    val university: String,
    val isRegistered: Boolean
) 