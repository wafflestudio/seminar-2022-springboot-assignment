package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.UserSeminar

data class Participant(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: List<UserSeminar>,
)