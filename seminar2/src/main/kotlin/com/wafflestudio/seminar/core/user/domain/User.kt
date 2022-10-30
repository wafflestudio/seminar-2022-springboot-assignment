package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.ParticipantEntity

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val participant: ParticipantEntity?,
)