package com.wafflestudio.seminar.core.user.api.request

data class RegisterParticipantRequest(
    val university: String? = "",
    val isRegistered: Boolean? = true
)
