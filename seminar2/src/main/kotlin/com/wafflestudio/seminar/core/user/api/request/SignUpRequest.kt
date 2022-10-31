package com.wafflestudio.seminar.core.user.api.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
)