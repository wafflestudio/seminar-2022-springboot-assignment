package com.wafflestudio.seminar.core.user.api.request

data class LoginRequest(
    val email: String,
    val password: String,
)
