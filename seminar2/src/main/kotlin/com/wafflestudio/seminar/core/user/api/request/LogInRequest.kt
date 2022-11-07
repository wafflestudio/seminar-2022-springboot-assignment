package com.wafflestudio.seminar.core.user.api.request

data class LogInRequest(
    val email: String?,
    val password: String?,
)