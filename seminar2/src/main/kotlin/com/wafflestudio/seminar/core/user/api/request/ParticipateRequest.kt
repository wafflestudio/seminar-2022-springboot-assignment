package com.wafflestudio.seminar.core.user.api.request

data class ParticipateRequest (
    val university: String = "",
    val isRegistered: Boolean = true 
)
