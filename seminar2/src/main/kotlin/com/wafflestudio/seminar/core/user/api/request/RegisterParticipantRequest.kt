package com.wafflestudio.seminar.core.user.api.request

data class RegisterParticipantRequest(
    var university: String = "",
    var isRegistered: Boolean = true,
)
