package com.wafflestudio.seminar.core.user.api.request

data class RegParRequest (
    val university: String? = null,
    val isRegistered: Boolean? = true
)