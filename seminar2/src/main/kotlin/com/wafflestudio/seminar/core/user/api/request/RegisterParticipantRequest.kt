package com.wafflestudio.seminar.core.user.api.request

data class RegisterParticipantRequest(
    val toBeParticipant: Boolean,
    val university: String? = "",
    val isRegistered: Boolean? = true
)
