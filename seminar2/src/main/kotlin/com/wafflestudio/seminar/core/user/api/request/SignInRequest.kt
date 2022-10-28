package com.wafflestudio.seminar.core.user.api.request

data class SignInRequest(
    val email: String,
    val password: String,
)
