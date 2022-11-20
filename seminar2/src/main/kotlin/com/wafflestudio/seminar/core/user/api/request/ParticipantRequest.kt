package com.wafflestudio.seminar.core.user.api.request

data class ParticipantRequest(
    val university : String = "",
    val isRegistered : Boolean = true
)
