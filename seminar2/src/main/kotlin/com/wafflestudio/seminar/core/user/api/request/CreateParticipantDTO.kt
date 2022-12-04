package com.wafflestudio.seminar.core.user.api.request

data class CreateParticipantDTO (
    val university: String,
    val is_registered: Boolean? = true,
)