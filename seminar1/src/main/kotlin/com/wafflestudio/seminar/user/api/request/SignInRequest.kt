package com.wafflestudio.seminar.user.api.request

data class SignInRequest(
    val email: String,
    val password: String
)