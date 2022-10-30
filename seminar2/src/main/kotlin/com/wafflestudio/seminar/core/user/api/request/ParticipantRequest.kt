package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.domain.User

data class ParticipantRequest(
    val user: User,
    val university: String = "",
    val isRegistered: Boolean = true,
)