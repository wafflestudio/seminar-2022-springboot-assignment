package com.wafflestudio.seminar.core.user.api.request

data class ParticipantRequest(
    val university: String = "",
    val isRegisterd: Boolean = true
) {
}