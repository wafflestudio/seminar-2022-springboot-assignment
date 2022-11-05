package com.wafflestudio.seminar.core.user.api.request

data class ParticipantRoleRequest (
    val university: String? = null,
    val isRegistered: Boolean? = true
)